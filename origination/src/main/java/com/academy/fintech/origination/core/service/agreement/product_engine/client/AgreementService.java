package com.academy.fintech.origination.core.service.agreement.product_engine.client;

import com.example.agreement.AgreementRequest;
import com.example.agreement.AgreementServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AgreementService {
    @GrpcClient("product-engine")
    private AgreementServiceGrpc.AgreementServiceBlockingStub agreementBlockingStub;

    public String createAgreement(AgreementRequest request) {
        return agreementBlockingStub.createAgreement(request).getAgreementNumber();
    }
}
