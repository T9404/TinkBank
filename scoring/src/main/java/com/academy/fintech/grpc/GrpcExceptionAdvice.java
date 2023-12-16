package com.academy.fintech.grpc;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@GrpcAdvice
public class GrpcExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GrpcExceptionAdvice.class);

    @GrpcExceptionHandler(RuntimeException.class)
    public StatusRuntimeException handleRuntimeException(Exception exception) {
        Status status = Status.INTERNAL.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Runtime exception", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleException(Exception exception) {
        Status status = Status.INTERNAL.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Exception", exception);
        return status.asRuntimeException(metadata);
    }

    private Metadata getMetadata() {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("timestamp", Metadata.ASCII_STRING_MARSHALLER), Instant.now().toString());
        return metadata;
    }
}
