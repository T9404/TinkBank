package com.academy.fintech.pe.integration.factory;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.agreement.enums.AgreementStatus;
import com.academy.fintech.pe.core.agreement.db.product.Product;

import java.math.BigDecimal;

public final class AgreementFactory {

    private AgreementFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static Agreement createAgreement(Product product) {
        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        agreement.setInterest(new BigDecimal(8));
        agreement.setTerm(12);
        agreement.setOriginationAmount(new BigDecimal(10000));
        agreement.setPrincipalAmount(new BigDecimal(500000));
        agreement.setStatus(AgreementStatus.NEW.getStatus());
        return agreement;
    }

}
