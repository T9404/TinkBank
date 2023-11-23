package com.academy.fintech.pe.grpc.application.v1;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.example.agreement.AgreementRequest;
import com.example.agreement.AgreementResponse;
import com.example.agreement.AgreementServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
@RequiredArgsConstructor
public class AgreementCreationServiceImpl extends AgreementServiceGrpc.AgreementServiceImplBase {
    private final AgreementCreationService agreementCreationService;

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
