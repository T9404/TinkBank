package com.academy.fintech.origination.core.service.application.payment_gate.client;

import com.example.disbursement.SendDisbursementRequest;
import com.example.disbursement.SendDisbursementServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class SendingDisbursementService {
    @GrpcClient("payment-gate")
    private SendDisbursementServiceGrpc.SendDisbursementServiceBlockingStub disbursementServiceBlockingStub;

    public String sendDisbursement(SendDisbursementRequest request) {
        return disbursementServiceBlockingStub.sendDisbursement(request).getStatus();
    }
}
