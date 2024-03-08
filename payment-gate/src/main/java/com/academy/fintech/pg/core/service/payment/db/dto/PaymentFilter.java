package com.academy.fintech.pg.core.service.payment.db.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record PaymentFilter(
        BigDecimal paymentAmount,
        String status,
        OffsetDateTime start,
        OffsetDateTime end
) {
}
