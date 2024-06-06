package com.academy.fintech.dwh.core.agreement.repository.entity;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record AgreementEntity(
        String dataAgreementId,
        String agreementNumber,
        String status,
        LocalDate businessDate,
        BigDecimal amount
) {
}
