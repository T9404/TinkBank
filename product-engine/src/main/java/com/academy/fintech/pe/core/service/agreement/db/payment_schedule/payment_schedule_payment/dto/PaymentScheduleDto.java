package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.dto;

import java.math.BigDecimal;

public record PaymentScheduleDto(
        BigDecimal monthlyPayment,
        BigDecimal annualInterestRate,
        BigDecimal principalPayment
) {
}
