package com.academy.fintech.pg.grps.payment.v1;

import com.academy.fintech.pg.core.service.payment.PaymentServiceV1;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import com.example.disbursement.SendDisbursementRequest;
import com.example.disbursement.SendDisbursementResponse;
import com.example.disbursement.SendDisbursementServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SendingDisbursementController extends SendDisbursementServiceGrpc.SendDisbursementServiceImplBase {
    private final PaymentServiceV1 paymentService;

    @Override
    public void sendDisbursement(SendDisbursementRequest request, StreamObserver<SendDisbursementResponse> responseObserver) {
        paymentService.sendDisbursement(request);

        responseObserver.onNext(
                SendDisbursementResponse.newBuilder()
                        .setStatus(PaymentStatus.PENDING.name())
                        .build()
        );

        responseObserver.onCompleted();
    }
}
