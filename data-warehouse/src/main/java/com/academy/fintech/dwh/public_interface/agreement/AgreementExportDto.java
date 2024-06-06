package com.academy.fintech.dwh.public_interface.agreement;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AgreementExportDto(
        String dataAgreementId,
        String agreementNumber,
        String status,
        long businessDate,
        BigDecimal amount
) {
}
