package com.academy.fintech.pe.core.service.agreement.util;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.example.agreement.AgreementRequest;

import java.math.BigDecimal;

public final class AgreementMapper {

    public static Agreement convertAgreementRequestToEntity(AgreementRequest request, Product product) {
        Agreement agreement = new Agreement();
        agreement.setClientId(request.getUserId());
        agreement.setProduct(product);
        agreement.setInterest(BigDecimal.valueOf(request.getInterest()));
        agreement.setTerm(request.getLoanTerm());
        agreement.setPrincipalAmount(BigDecimal.valueOf(request.getDisbursementAmount() + request.getOriginationAmount()));
        agreement.setOriginationAmount(BigDecimal.valueOf(request.getOriginationAmount()));
        agreement.setStatus(AgreementStatus.NEW.getStatus());
        return agreement;
    }
}
