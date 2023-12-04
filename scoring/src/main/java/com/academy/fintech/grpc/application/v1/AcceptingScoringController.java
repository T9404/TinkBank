package com.academy.fintech.grpc.application.v1;

import com.academy.fintech.core.service.approval.EstimateService;
import com.example.payment.AcceptingScoringRequest;
import com.example.payment.AcceptingScoringResponse;
import com.example.payment.AcceptingScoringServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AcceptingScoringController extends AcceptingScoringServiceGrpc.AcceptingScoringServiceImplBase {
    private final EstimateService estimateService;

    @Override
    public void acceptScoring(AcceptingScoringRequest request, StreamObserver<AcceptingScoringResponse> responseObserver) {
        int score = estimateService.estimate(request);
        AcceptingScoringResponse response = AcceptingScoringResponse.newBuilder()
            .setResult(score)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
