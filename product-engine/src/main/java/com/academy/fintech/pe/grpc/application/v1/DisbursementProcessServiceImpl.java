package com.academy.fintech.pe.grpc.application.v1;

import com.academy.fintech.pe.core.service.agreement.AgreementDisbursementService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import proto.DisbursementProcessGrpc;
import proto.DisbursementRequest;
import proto.DisbursementResponse;

@GrpcService
@RequiredArgsConstructor
public class DisbursementProcessServiceImpl extends DisbursementProcessGrpc.DisbursementProcessImplBase {
    private final AgreementDisbursementService agreementDisbursementService;

    @Override
    public void acceptDisbursementProcess(DisbursementRequest request, StreamObserver<DisbursementResponse> responseObserver) {
        var message = agreementDisbursementService.acceptDisbursementProcess(request);
        DisbursementResponse response = DisbursementResponse.newBuilder()
            .setMessage(message)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
