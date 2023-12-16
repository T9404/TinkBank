package com.academy.fintech.core.service.approval.product.engine.client;

import com.example.payment.LatePaymentRequest;
import com.example.payment.LatePaymentResponse;
import com.example.payment.LatePaymentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LatePaymentService {
    @GrpcClient("grpc-server")
    private LatePaymentServiceGrpc.LatePaymentServiceBlockingStub latePaymentStub;

    public List<Timestamp> getLatePayment(LatePaymentRequest agreementNumber) {
        LatePaymentResponse response = latePaymentStub.getLatePayment(agreementNumber);
        return response.getTimestampsList();
    }
}
