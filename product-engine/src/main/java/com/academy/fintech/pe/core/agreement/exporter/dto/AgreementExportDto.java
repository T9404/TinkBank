package com.academy.fintech.pe.core.agreement.exporter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record AgreementExportDto(
        String id,
        String agreementNumber,
        String status,
        LocalDate businessDate,
        BigDecimal amount
) {
}
