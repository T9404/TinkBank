package com.academy.fintech.pe.grpc;

import com.academy.fintech.pe.core.agreement.exception.AgreementNotFoundException;
import com.academy.fintech.pe.core.agreement.exception.AgreementValidationException;
import com.academy.fintech.pe.core.agreement.exception.PaymentInFutureException;
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

    @GrpcExceptionHandler(AgreementNotFoundException.class)
    public StatusRuntimeException handleAgreementNotFound(Exception exception) {
        Status status = Status.NOT_FOUND.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Agreement not found", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(PaymentInFutureException.class)
    public StatusRuntimeException handlePaymentInFuture(Exception exception) {
        Status status = Status.INVALID_ARGUMENT.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Payment date cannot be in future", exception);
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(AgreementValidationException.class)
    public StatusRuntimeException handleCashLoanValidationException(Exception exception) {
        Status status = Status.INVALID_ARGUMENT.withDescription(exception.getMessage());
        Metadata metadata = getMetadata();
        logger.error("Cash loan validation exception", exception);
        return status.asRuntimeException(metadata);
    }

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
