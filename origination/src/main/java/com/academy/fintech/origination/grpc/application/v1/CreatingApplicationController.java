package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.application.CreationApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CreatingApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {
    private final CreationApplicationService creationApplicationService;

    @Override
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        var applicationId = creationApplicationService.createApplication(request);
        responseObserver.onNext(
                ApplicationResponse.newBuilder()
                        .setApplicationId(applicationId)
                        .build()
        );
        responseObserver.onCompleted();
    }
}
