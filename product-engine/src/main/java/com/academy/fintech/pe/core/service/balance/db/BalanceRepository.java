package com.academy.fintech.pe.core.service.balance.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {
    Optional<Balance> findByAgreementIdAndType(String agreementId, String type);
}
