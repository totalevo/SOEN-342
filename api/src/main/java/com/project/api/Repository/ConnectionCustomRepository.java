package com.project.api.Repository;

import com.project.api.Entity.Connection;
import com.project.api.Entity.SearchParameters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        if (searchParameters.getDaysOfOperation() != null) {
            predicates.add(cb.equal(connectionRoot.get("daysOfOperation"), searchParameters.getDaysOfOperation()));
        }
        if (searchParameters.getFirstClassRate() != null) {
            predicates.add(cb.equal(connectionRoot.get("firstClassRate"), searchParameters.getFirstClassRate()));
        }
        if (searchParameters.getSecondClassRate() != null) {
            predicates.add(cb.equal(connectionRoot.get("secondClassRate"), searchParameters.getSecondClassRate()));
        }
        if (searchParameters.getSortBy() != null && searchParameters.getSortOrder() != null) {
            cq.orderBy((searchParameters.getSortOrder().equals("asc")) ? cb.asc(connectionRoot.get(searchParameters.getSortBy()))
                    : cb.desc(connectionRoot.get(searchParameters.getSortBy())));
        }
        cq.select(connectionRoot).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
