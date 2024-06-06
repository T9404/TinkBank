package com.academy.fintech.origination.grpc.exception;

import com.academy.fintech.origination.core.application.exception.ApplicationAlreadyExists;
import com.academy.fintech.origination.core.application.exception.ApplicationNotFoundException;
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

    @GrpcExceptionHandler(ApplicationAlreadyExists.class)
    public StatusRuntimeException handleApplicationAlreadyExists(ApplicationAlreadyExists exception) {
        Status status = Status.ALREADY_EXISTS.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Application already exists", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(ApplicationNotFoundException.class)
    public StatusRuntimeException handleApplicationNotFoundException(ApplicationNotFoundException exception) {
        Status status = Status.NOT_FOUND.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Application not found", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(RuntimeException.class)
    public StatusRuntimeException handleRuntimeException(RuntimeException exception) {
        Status status = Status.INTERNAL.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Internal server error", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleException(Exception exception) {
        Status status = Status.INTERNAL.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Internal server error", exception);
        return status.asRuntimeException(metadata);
    }

    private Metadata getMetadata() {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("timestamp", Metadata.ASCII_STRING_MARSHALLER), Instant.now().toString());
        return metadata;
    }
}
