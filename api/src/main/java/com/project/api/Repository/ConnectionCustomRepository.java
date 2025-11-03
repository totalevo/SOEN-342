package com.project.api.Repository;

import com.project.api.Class.ConnectionDuration;
import com.project.api.Class.DaysBitMap;
import com.project.api.Entity.Connection;
import com.project.api.Entity.IndirectResultContext;
import com.project.api.Entity.SearchParameters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
    public List<List<IndirectResultContext>> findIndirectConnections(SearchParameters searchParameters) {
        // Find all connections from startCity to any city, beginning of iteration
        String leg1Query = "SELECT c FROM Connection c WHERE c.departureCity = :startCity";
        List<Connection> leg1List = entityManager.createQuery(leg1Query, Connection.class)
                .setParameter("startCity", searchParameters.getDepartureCity())
                .getResultList();


        // DEBUG for Leg1
        System.out.println("Leg1 list (from " + searchParameters.getDepartureCity() + "): " + leg1List.size());
        for (Connection leg1 : leg1List) {
            System.out.println("  Leg1: " + leg1.getDepartureCity() + " -> " + leg1.getArrivalCity() + " (" + leg1.getConnectionId() + ")");
        }

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

            // DEBUG for Leg2
            System.out.println("    For Leg1: " + leg1.getDepartureCity() + "->" + leg1.getArrivalCity() + " (" + leg1.getConnectionId() + ")");
            System.out.println("      Leg2 candidates count: " + leg2List.size());
            for (Connection leg2 : leg2List) {
                System.out.println("        Leg2: " + leg2.getDepartureCity() + "->" + leg2.getArrivalCity() + " (" + leg2.getConnectionId() + ")");
            }

            for (Connection leg2 : leg2List) {
                // To avoid cycles
                if (!leg2.getId().equals(leg1.getId())) {
                    if (!isLayoverAllowed(leg1.getArrivalTime(), leg2.getDepartureTime())) {
                        continue; // we wanna reject this pair based on policy
                    }
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
                    if (!isLayoverAllowed(leg1.getArrivalTime(), leg2.getDepartureTime())) {
                        continue; // reject leg2
                    }
                    // Find final leg (leg3) from leg2's arrival city to endCity
                    String leg3Query = "SELECT c FROM Connection c WHERE c.departureCity = :transitCity2 AND c.arrivalCity = :endCity AND c.departureTime > :prevArrival2";
                    List<Connection> leg3List = entityManager.createQuery(leg3Query, Connection.class)
                            .setParameter("transitCity2", leg2.getArrivalCity())
                            .setParameter("endCity", searchParameters.getArrivalCity())
                            .setParameter("prevArrival2", leg2.getArrivalTime())
                            .getResultList();

                    for (Connection leg3 : leg3List) {
                        if (!isLayoverAllowed(leg2.getArrivalTime(), leg3.getDepartureTime())) {
                            continue; // reject leg3
                        }
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
        List<IndirectResultContext> oneStopResults = new ArrayList<>();
        for (List<Connection> connectionList : oneStopConnections) {

            int[] timeBetweenConnections = new int[connectionList.size() - 1];
            for (int i = 0; i < connectionList.size() - 1; i++) {
                String arrivalTime = connectionList.get(i).getArrivalTime();
                if (arrivalTime.contains("(+1d)")) {
                    arrivalTime = arrivalTime.replaceAll("\\(\\+1d(ay)?\\)", "").trim();
                }
                String[] arrivalParts = arrivalTime.split(":");
                String[] departureParts = connectionList.get(i + 1).getDepartureTime().split(":");
                timeBetweenConnections[i] = ConnectionDuration.getTotalMinutes(arrivalParts, departureParts);
            }
            int totalDuration = connectionList.stream().mapToInt(Connection::getDurationMinutes).sum() + Arrays.stream(timeBetweenConnections).sum();
            int totalFirstClassRate = connectionList.stream().mapToInt(c -> c.getFirstClassRate().intValue()).sum();
            int totalSecondClassRate = connectionList.stream().mapToInt(c -> c.getSecondClassRate().intValue()).sum();
            oneStopResults.add(new IndirectResultContext(connectionList, totalDuration, timeBetweenConnections, totalFirstClassRate, totalSecondClassRate));
        }
        List<IndirectResultContext> twoStopResults = new ArrayList<>();
        for (List<Connection> connectionList : twoStopConnections) {
            int[] timeBetweenConnections = new int[connectionList.size() - 1];
            for (int i = 0; i < connectionList.size() - 1; i++) {
                String arrivalTime = connectionList.get(i).getArrivalTime();
                if (arrivalTime.contains("(+1d)")) {
                    arrivalTime = arrivalTime.replaceAll("\\(\\+1d(ay)?\\)", "").trim();
                }
                String[] arrivalParts = arrivalTime.split(":");
                String[] departureParts = connectionList.get(i + 1).getDepartureTime().split(":");
                timeBetweenConnections[i] = ConnectionDuration.getTotalMinutes(arrivalParts, departureParts);
            }
            int totalDuration = connectionList.stream().mapToInt(Connection::getDurationMinutes).sum() + Arrays.stream(timeBetweenConnections).sum();
            int totalFirstClassRate = connectionList.stream().mapToInt(c -> c.getFirstClassRate().intValue()).sum();
            int totalSecondClassRate = connectionList.stream().mapToInt(c -> c.getSecondClassRate().intValue()).sum();
            twoStopResults.add(new IndirectResultContext(connectionList, totalDuration, timeBetweenConnections, totalFirstClassRate, totalSecondClassRate));
        }

        if (searchParameters.getSortBy() != null && searchParameters.getSortOrder() != null) {
            if (searchParameters.getSortBy().equals("durationMinutes")) {
                if (searchParameters.getSortOrder().equals("asc")) {
                    oneStopResults.sort((a, b) -> Integer.compare(a.getTotalDuration(), b.getTotalDuration()));
                    twoStopResults.sort((a, b) -> Integer.compare(a.getTotalDuration(), b.getTotalDuration()));
                } else {
                    oneStopResults.sort((a, b) -> Integer.compare(b.getTotalDuration(), a.getTotalDuration()));
                    twoStopResults.sort((a, b) -> Integer.compare(b.getTotalDuration(), a.getTotalDuration()));
                }
            } else if (searchParameters.getSortBy().equals("firstClassRate")) {
                if (searchParameters.getSortOrder().equals("asc")) {
                    oneStopResults.sort((a, b) -> Integer.compare(a.getTotalFirstClassRate(), b.getTotalFirstClassRate()));
                    twoStopResults.sort((a, b) -> Integer.compare(a.getTotalFirstClassRate(), b.getTotalFirstClassRate()));
                } else {
                    oneStopResults.sort((a, b) -> Integer.compare(b.getTotalFirstClassRate(), a.getTotalFirstClassRate()));
                    twoStopResults.sort((a, b) -> Integer.compare(b.getTotalFirstClassRate(), a.getTotalFirstClassRate()));
                }
            } else if (searchParameters.getSortBy().equals("secondClassRate")) {
                if (searchParameters.getSortOrder().equals("asc")) {
                    oneStopResults.sort((a, b) -> Integer.compare(a.getTotalSecondClassRate(), b.getTotalSecondClassRate()));
                    twoStopResults.sort((a, b) -> Integer.compare(a.getTotalSecondClassRate(), b.getTotalSecondClassRate()));
                } else {
                    oneStopResults.sort((a, b) -> Integer.compare(b.getTotalSecondClassRate(), a.getTotalSecondClassRate()));
                    twoStopResults.sort((a, b) -> Integer.compare(b.getTotalSecondClassRate(), a.getTotalSecondClassRate()));
                }
            }
        }

        // Return result with 1-stop at index 0 and 2-stop at index 1
        List<List<IndirectResultContext>> result = new ArrayList<>();

        result.add(oneStopResults);
        result.add(twoStopResults);

        return result;
    }

    // Helper methods to deal with the layovers
    private int toMinutes(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private String cleanTime(String t) {
        // Strip "(+1d)" if present
        return t.contains("(+1d)") ? t.replaceAll("\\(\\+1d(ay)?\\)", "").trim() : t;
    }

    private boolean isDaytime(int minutesOfDay) {
        return minutesOfDay >= 6 * 60 && minutesOfDay < 22 * 60;
    }

    private boolean isLayoverAllowed(String arrivalTimeRaw, String nextDepartureTimeRaw) {
        String arr = cleanTime(arrivalTimeRaw);
        String dep = cleanTime(nextDepartureTimeRaw);

        int arrMin = toMinutes(arr);
        int depMin = toMinutes(dep);

        // current indirect logic already ensures dep > arr (same-day sequence)
        int layover = depMin - arrMin;

        // Disallow unrealistically short layovers
        if (layover < 10) return false;

        // Determine policy by ARRIVAL time
        boolean day = isDaytime(arrMin);

        if (day) {
            // Daytime: 60–120 minutes
            return layover >= 60 && layover <= 120;
        } else {
            // After-hours: ≤ 30 minutes
            return layover <= 30;
        }
    }

}
