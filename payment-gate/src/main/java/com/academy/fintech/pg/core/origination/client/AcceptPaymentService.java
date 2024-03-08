package com.academy.fintech.pg.core.origination.client;

import com.example.payment.AcceptPaymentRequest;
import com.example.payment.AcceptingPaymentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AcceptPaymentService {
    @GrpcClient("origination")
    private AcceptingPaymentServiceGrpc.AcceptingPaymentServiceBlockingStub acceptingPaymentServiceBlockingStub;

    public void acceptPayment(AcceptPaymentRequest request) {
        acceptingPaymentServiceBlockingStub.acceptPayment(request);
    }
}
