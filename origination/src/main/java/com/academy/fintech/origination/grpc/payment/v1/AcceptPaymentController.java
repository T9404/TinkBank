package com.academy.fintech.origination.grpc.payment.v1;

import com.academy.fintech.origination.core.service.payment.AcceptPaymentService;
import com.example.payment.AcceptPaymentRequest;
import com.example.payment.AcceptingPaymentServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AcceptPaymentController extends AcceptingPaymentServiceGrpc.AcceptingPaymentServiceImplBase {
    private final AcceptPaymentService acceptPaymentService;

    @Override
    public void acceptPayment(AcceptPaymentRequest request, StreamObserver<Empty> responseObserver) {
        acceptPaymentService.acceptPayment(request);
        responseObserver.onNext(
                Empty.newBuilder().build()
        );
        responseObserver.onCompleted();
    }
}
