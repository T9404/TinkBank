package com.academy.fintech.core.service.approval.product.engine.client;

import com.example.payment.PeriodPaymentRequest;
import com.example.payment.PeriodPaymentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class PeriodPaymentService {

    @GrpcClient("grpc-server")
    private PeriodPaymentServiceGrpc.PeriodPaymentServiceBlockingStub periodPaymentStub;

    public int getPeriodPayment(PeriodPaymentRequest agreementNumber) {
        return periodPaymentStub.getPeriodPayment(agreementNumber).getPeriodPayment();
    }
}
