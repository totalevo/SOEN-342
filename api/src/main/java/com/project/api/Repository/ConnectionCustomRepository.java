package com.project.api.Repository;

import com.project.api.Class.ConnectionDuration;
import com.project.api.Class.DaysBitMap;
import com.project.api.Entity.Connection;
import com.project.api.Entity.SearchParameters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.project.api.Class.DaysBitMap.operatesOnDay;

@Repository
public class ConnectionCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Connection> findConnectionsDynamically(SearchParameters searchParameters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Connection> cq = cb.createQuery(Connection.class);
        Root<Connection> connectionRoot = cq.from(Connection.class);

        List<Predicate> predicates = new ArrayList<>();
        if (searchParameters.getDepartureCity() != null) {
            predicates.add(cb.equal(connectionRoot.get("departureCity"), searchParameters.getDepartureCity()));
        }
        if (searchParameters.getArrivalCity() != null) {
            predicates.add(cb.equal(connectionRoot.get("arrivalCity"), searchParameters.getArrivalCity()));
        }
        if (searchParameters.getDepartureTime() != null) {
            predicates.add(cb.equal(connectionRoot.get("departureTime"), searchParameters.getDepartureTime()));
        }
        if (searchParameters.getArrivalTime() != null) {
            predicates.add(cb.equal(connectionRoot.get("arrivalTime"), searchParameters.getArrivalTime()));
        }
        if (searchParameters.getTrainType() != null) {
            predicates.add(cb.equal(connectionRoot.get("trainType"), searchParameters.getTrainType()));
        }
        if (searchParameters.getBitmapDays() != 0) {
            predicates.add(cb.notEqual(
                    cb.function(
                            "BITAND",
                            Integer.class,
                            connectionRoot.get("bitmapDaysOfOperation"),
                            cb.literal(searchParameters.getBitmapDays())
                    ),
                    0
            ));
        }
        if (searchParameters.getFirstClassRate() != null) {
            predicates.add(cb.equal(connectionRoot.get("firstClassRate"), searchParameters.getFirstClassRate()));
        }
        if (searchParameters.getSecondClassRate() != null) {
            predicates.add(cb.equal(connectionRoot.get("secondClassRate"), searchParameters.getSecondClassRate()));
        }
        if (searchParameters.getDuration() != 0) {
            predicates.add(cb.equal(connectionRoot.get("durationMinutes"), searchParameters.getDuration()));
        }
        if (searchParameters.getSortBy() != null && searchParameters.getSortOrder() != null) {
            cq.orderBy((searchParameters.getSortOrder().equals("asc")) ? cb.asc(connectionRoot.get(searchParameters.getSortBy()))
                    : cb.desc(connectionRoot.get(searchParameters.getSortBy())));
        }
        cq.select(connectionRoot).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    // Calculate the possible connections, such as 1-stop, 2-stop
    public List<List<List<Connection>>> findIndirectConnections(SearchParameters searchParameters) {
        // Find all connections from startCity to any city, beginning of iteration
        String leg1Query = "SELECT c FROM Connection c WHERE c.departureCity = :startCity";
        List<Connection> leg1List = entityManager.createQuery(leg1Query, Connection.class)
                .setParameter("startCity", searchParameters.getDepartureCity())
                .getResultList();

        List<List<Connection>> oneStopConnections = new ArrayList<>();
        List<List<Connection>> twoStopConnections = new ArrayList<>();

        // Find 1-stop connections
        for (Connection leg1 : leg1List) {
            String leg2Query = "SELECT c FROM Connection c WHERE c.departureCity = :transitCity AND c.arrivalCity = :endCity AND c.departureTime > :prevArrival";
            List<Connection> leg2List = entityManager.createQuery(leg2Query, Connection.class)
                    .setParameter("transitCity", leg1.getArrivalCity())
                    .setParameter("endCity", searchParameters.getArrivalCity())
                    .setParameter("prevArrival", leg1.getArrivalTime())
                    .getResultList();

            for (Connection leg2 : leg2List) {
                // To avoid cycles
                if (!leg2.getId().equals(leg1.getId())) {
                    List<Connection> combo = new ArrayList<>();
                    combo.add(leg1);
                    combo.add(leg2);
                    oneStopConnections.add(combo);
                }
            }
        }

        // Find 2-stop connections
        for (Connection leg1 : leg1List) {
            // Find intermediate connections (leg2) from leg1's arrival city to anywhere (not endCity)
            String leg2Query = "SELECT c FROM Connection c WHERE c.departureCity = :transitCity1 AND c.arrivalCity != :endCity AND c.departureTime > :prevArrival1";
            List<Connection> leg2List = entityManager.createQuery(leg2Query, Connection.class)
                    .setParameter("transitCity1", leg1.getArrivalCity())
                    .setParameter("endCity", searchParameters.getArrivalCity())
                    .setParameter("prevArrival1", leg1.getArrivalTime())
                    .getResultList();

            for (Connection leg2 : leg2List) {
                // To avoid cycles and ensure leg2 is different from leg1
                if (!leg2.getId().equals(leg1.getId())) {
                    // Find final leg (leg3) from leg2's arrival city to endCity
                    String leg3Query = "SELECT c FROM Connection c WHERE c.departureCity = :transitCity2 AND c.arrivalCity = :endCity AND c.departureTime > :prevArrival2";
                    List<Connection> leg3List = entityManager.createQuery(leg3Query, Connection.class)
                            .setParameter("transitCity2", leg2.getArrivalCity())
                            .setParameter("endCity", searchParameters.getArrivalCity())
                            .setParameter("prevArrival2", leg2.getArrivalTime())
                            .getResultList();

                    for (Connection leg3 : leg3List) {
                        // To avoid cycles - ensure all three legs are different
                        if (!leg3.getId().equals(leg1.getId()) && !leg3.getId().equals(leg2.getId())) {
                            List<Connection> combo = new ArrayList<>();
                            combo.add(leg1);
                            combo.add(leg2);
                            combo.add(leg3);
                            twoStopConnections.add(combo);
                        }
                    }
                }
            }
        }

        // Return result with 1-stop at index 0 and 2-stop at index 1
        List<List<List<Connection>>> result = new ArrayList<>();
        result.add(oneStopConnections);
        result.add(twoStopConnections);

        return result;
    }
}
