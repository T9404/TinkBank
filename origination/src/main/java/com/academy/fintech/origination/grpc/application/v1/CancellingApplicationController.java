package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.application.CancelApplicationServiceGrpc;
import com.academy.fintech.origination.core.service.application.CancellingApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CancellingApplicationController extends CancelApplicationServiceGrpc.CancelApplicationServiceImplBase {
    private final CancellingApplicationService cancellingApplicationService;

    @Override
    public void cancel(CancelApplicationRequest request, StreamObserver<CancelApplicationResponse> responseObserver) {
        cancellingApplicationService.cancelApplication(request.getApplicationId());
        responseObserver.onNext(
                CancelApplicationResponse.newBuilder()
                        .setMessage("Application cancelled")
                        .build()
        );
        responseObserver.onCompleted();
    }
}
