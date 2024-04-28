package com.academy.fintech.origination.core.service.agreement;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.service.agreement.product_engine.client.AgreementService;
import com.example.agreement.AgreementRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAgreementService {
    private final AgreementService agreementService;

    /**
     * For now data is hardcoded, because we should get it from web
     */
    public String createAgreement(Application application) {
        var agreementRequest = AgreementRequest.newBuilder()
                .setUserId(application.getUsers().getId())
                .setLoanTerm(12)
                .setDisbursementAmount(application.getRequestedDisbursementAmount())
                .setOriginationAmount(5000)
                .setInterest(10)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .build();

        return agreementService.createAgreement(agreementRequest);
    }
}
