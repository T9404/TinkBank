package com.academy.fintech.pg.core.service.payment.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID>, FilterPaymentRepository {
    Optional<PaymentEntity> findBySenderAccountId(String senderAccountId);
}
