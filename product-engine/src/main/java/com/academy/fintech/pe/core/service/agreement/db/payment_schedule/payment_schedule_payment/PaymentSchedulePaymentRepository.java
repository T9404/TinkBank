package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentSchedulePaymentRepository extends JpaRepository<PaymentSchedulePayment, UUID> {
    List<PaymentSchedulePayment> findByPaymentScheduleId(UUID paymentScheduleId);
}
