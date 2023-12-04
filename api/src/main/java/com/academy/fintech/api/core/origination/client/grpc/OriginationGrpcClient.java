package com.academy.fintech.api.core.origination.client.grpc;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.ApplicationServiceGrpc.ApplicationServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class OriginationGrpcClient {
    private final ApplicationServiceBlockingStub stub;

    public OriginationGrpcClient(OriginationGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ApplicationServiceGrpc.newBlockingStub(channel);
    }

    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            return stub.create(applicationRequest);
        } catch (StatusRuntimeException e) {
            // log.log(Level.SEVERE, "Got error from Origination by request: " + applicationRequest.toString() + e.getTrailers().toString(), e);
            throw e;
        }
    }

}
