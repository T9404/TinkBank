package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {
    @Query(value = "SELECT MAX(version) FROM payment_schedule WHERE agreement_number = ?1", nativeQuery = true)
    Optional<Integer> getLatestVersionByAgreementNumber(UUID agreementNumber);
}
