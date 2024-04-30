package com.academy.fintech.pe.core.agreement.exporter.exception;

public class KafkaWaitingException extends RuntimeException {
    public KafkaWaitingException(String message, Throwable throwable) {
        super(message);
    }
}
