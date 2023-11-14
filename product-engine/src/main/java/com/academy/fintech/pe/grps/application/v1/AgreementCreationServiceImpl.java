package com.academy.fintech.pe.grps.application.v1;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.example.agreement.AgreementRequest;
import com.example.agreement.AgreementResponse;
import com.example.agreement.AgreementServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class AgreementCreationServiceImpl extends AgreementServiceGrpc.AgreementServiceImplBase {
    private AgreementCreationService agreementCreationService;

    @Autowired
    public void setAgreementCreationService(AgreementCreationService agreementCreationService) {
        this.agreementCreationService = agreementCreationService;
    }

    @Override
    public void createAgreement(AgreementRequest request, StreamObserver<AgreementResponse> responseObserver) {
        var agreementNumber = agreementCreationService.createAgreement(request);
        AgreementResponse response = AgreementResponse.newBuilder()
            .setAgreementNumber(agreementNumber)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
