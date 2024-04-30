package com.academy.fintech.pe.grpc.payment.v1;

import com.academy.fintech.pe.core.agreement.PaymentServiceV1;
import com.example.payment.ClientPaymentRequest;
import com.example.payment.ClientPaymentResponse;
import com.example.payment.ClientPaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AddPaymentController extends ClientPaymentServiceGrpc.ClientPaymentServiceImplBase {
    private final PaymentServiceV1 paymentService;

    @Override
    public void addClientPayment(ClientPaymentRequest request, StreamObserver<ClientPaymentResponse> responseObserver) {
        paymentService.addPayment(request);

        ClientPaymentResponse response = ClientPaymentResponse.newBuilder()
                .setStatus("SUCCESS")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
