package com.academy.fintech.origination.core.service.application.exception;

public class ApplicationAlreadyExists extends RuntimeException {
    public ApplicationAlreadyExists() {
        super("Application already exists");
    }
}
