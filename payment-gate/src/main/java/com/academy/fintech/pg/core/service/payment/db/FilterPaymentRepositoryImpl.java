package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.service.payment.db.dto.PaymentFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class FilterPaymentRepositoryImpl implements FilterPaymentRepository {
    private final EntityManager entityManager;

    @Override
    public List<PaymentEntity> findAll(PaymentFilter filter) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(PaymentEntity.class);
        var root = query.from(PaymentEntity.class);
        var predicates = new ArrayList<>();

        if (filter.paymentAmount() != null) {
            predicates.add(cb.equal(root.get("paymentAmount"), filter.paymentAmount()));
        }

        if (filter.status() != null && !filter.status().isBlank()) {
            predicates.add(cb.equal(root.get("status"), filter.status()));
        }

        if (filter.start() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("backoffTime"), filter.start()));
        }

        if (filter.end() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("backoffTime"), filter.end()));
        }

        query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }
}
