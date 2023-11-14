package com.academy.fintech.pe.core.service.agreement.util;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.enums.AgreementStatus;
import com.example.agreement.AgreementRequest;

import java.math.BigDecimal;
import java.util.UUID;

public final class AgreementMapper {

    public static Agreement convertAgreementRequestToEntity(AgreementRequest request) {
        Agreement agreement = new Agreement();
        agreement.setClientId(UUID.fromString(request.getUserId()));
        agreement.setProductCode(request.getProductCode() + " " + request.getProductVersion());
        agreement.setInterest(BigDecimal.valueOf(request.getInterest()));
        agreement.setTerm(request.getLoanTerm());
        agreement.setPrincipalAmount(BigDecimal.valueOf(request.getDisbursementAmount() + request.getOriginationAmount()));
        agreement.setOriginationAmount(BigDecimal.valueOf(request.getOriginationAmount()));
        agreement.setStatus(AgreementStatus.NEW.getStatus());
        return agreement;
    }
}
