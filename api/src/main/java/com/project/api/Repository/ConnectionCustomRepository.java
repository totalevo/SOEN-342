package com.project.api.Repository;

import com.project.api.Class.ConnectionDuration;
import com.project.api.Class.DaysBitMap;
import com.project.api.Entity.Connection;
import com.project.api.Entity.SearchParameters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
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

    public List<List<Connection>> findIndirectConnections(String startCity, String endCity) {
        // Find all connections from startCity to any city
        String leg1Query = "SELECT c FROM Connection c WHERE c.departureCity = :startCity";
        List<Connection> leg1List = entityManager.createQuery(leg1Query, Connection.class)
                .setParameter("startCity", startCity)
                .getResultList();

        List<List<Connection>> result = new ArrayList<>();

        // For each leg1, we need to  find a second city that departs from the arrival city of to the endCity
        for (Connection leg1 : leg1List) {
            String leg2Query = "SELECT c FROM Connection c WHERE c.departureCity = :transitCity AND c.arrivalCity = :endCity AND c.departureTime > :prevArrival";
            List<Connection> leg2List = entityManager.createQuery(leg2Query, Connection.class)
                    .setParameter("transitCity", leg1.getArrivalCity())
                    .setParameter("endCity", endCity)
                    .setParameter("prevArrival", leg1.getArrivalTime()) // only allow next connection after first has arrived
                    .getResultList();

            for (Connection leg2 : leg2List) {
                // To avoid cycles
                if (!leg2.getId().equals(leg1.getId())) {
                    List<Connection> combo = new ArrayList<>();
                    combo.add(leg1);
                    combo.add(leg2);
                    result.add(combo);
                }
            }
        }
        return result;
    }
}
