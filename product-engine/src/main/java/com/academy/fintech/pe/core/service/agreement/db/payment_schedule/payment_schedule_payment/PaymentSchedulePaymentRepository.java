package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentSchedulePaymentRepository extends JpaRepository<PaymentSchedulePayment, String> {
    List<PaymentSchedulePayment> findByPaymentScheduleId(String paymentScheduleId);
    List<PaymentSchedulePayment> findAllByPaymentSchedule(PaymentSchedule paymentSchedule);
}
