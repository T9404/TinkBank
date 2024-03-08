package com.academy.fintech.pg.core.service.payment.product_engine.client;

import com.example.payment.ClientPaymentRequest;
import com.example.payment.ClientPaymentResponse;
import com.example.payment.ClientPaymentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ClientPaymentService {
    @GrpcClient("product-engine")
    private ClientPaymentServiceGrpc.ClientPaymentServiceBlockingStub clientPaymentServiceBlockingStub;

    public ClientPaymentResponse sendPaymentRequest(ClientPaymentRequest request) {
        return clientPaymentServiceBlockingStub.addClientPayment(request);
    }

}
