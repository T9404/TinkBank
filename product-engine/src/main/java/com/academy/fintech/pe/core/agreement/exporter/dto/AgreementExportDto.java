package com.academy.fintech.pe.core.agreement.exporter.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AgreementExportDto(
        String id,
        String agreementNumber,
        String status,
        long businessDate,
        BigDecimal amount
) {
}
