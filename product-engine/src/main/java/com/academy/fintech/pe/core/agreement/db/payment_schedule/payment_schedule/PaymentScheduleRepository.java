package com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, String> {
    Optional<PaymentSchedule> findFirstByAgreementOrderByVersionDesc(Agreement agreement);
    List<PaymentSchedule> findAllByAgreement(Agreement agreement);
}
