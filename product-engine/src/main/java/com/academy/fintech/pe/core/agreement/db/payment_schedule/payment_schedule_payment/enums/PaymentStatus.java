package com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    FUTURE("FUTURE"),
    PAID("PAID"),
    OVERDUE("OVERDUE");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

}
