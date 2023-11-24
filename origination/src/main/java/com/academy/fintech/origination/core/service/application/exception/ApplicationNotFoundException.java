package com.academy.fintech.origination.core.service.application.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException() {
        super("Application not found");
    }
}
