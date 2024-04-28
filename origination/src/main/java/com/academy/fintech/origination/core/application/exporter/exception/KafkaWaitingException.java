package com.academy.fintech.origination.core.application.exporter.exception;

public class KafkaWaitingException extends RuntimeException {
    public KafkaWaitingException(String message, Throwable throwable) {
        super(message);
    }
}
