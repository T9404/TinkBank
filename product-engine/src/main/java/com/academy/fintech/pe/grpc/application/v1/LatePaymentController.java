package com.academy.fintech.pe.grpc.application.v1;

import com.academy.fintech.pe.core.agreement.PaymentServiceV1;
import com.example.payment.LatePaymentRequest;
import com.example.payment.LatePaymentResponse;
import com.example.payment.LatePaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class LatePaymentController extends LatePaymentServiceGrpc.LatePaymentServiceImplBase {
    private final PaymentServiceV1 paymentService;

    @Override
    public void getLatePayment(LatePaymentRequest request, StreamObserver<LatePaymentResponse> responseObserver) {
        LatePaymentResponse response = LatePaymentResponse.newBuilder()
            .addAllTimestamps(paymentService.getLatePayment(request.getUserId()))
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
