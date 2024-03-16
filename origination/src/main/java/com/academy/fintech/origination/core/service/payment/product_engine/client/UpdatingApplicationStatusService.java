package com.academy.fintech.origination.core.service.payment.product_engine.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.DisbursementProcessGrpc;
import proto.DisbursementRequest;
import proto.DisbursementResponse;

@Service
public class UpdatingApplicationStatusService {
    @GrpcClient("product-engine")
    private DisbursementProcessGrpc.DisbursementProcessBlockingStub disbursementProcessBlockingStub;

    public DisbursementResponse updateApplicationStatus(DisbursementRequest request) {
        return disbursementProcessBlockingStub.acceptDisbursementProcess(request);
    }
}
