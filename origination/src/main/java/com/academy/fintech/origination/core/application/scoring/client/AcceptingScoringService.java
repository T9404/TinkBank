package com.academy.fintech.origination.core.application.scoring.client;

import com.example.payment.AcceptingScoringRequest;
import com.example.payment.AcceptingScoringServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AcceptingScoringService {
    @GrpcClient("grpc-server")
    private AcceptingScoringServiceGrpc.AcceptingScoringServiceBlockingStub acceptingScoringStub;

    public int acceptScoring(AcceptingScoringRequest request) {
        return acceptingScoringStub.acceptScoring(request).getResult();
    }
}
