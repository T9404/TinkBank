package com.academy.fintech.origination.core.application.exception;

public class ApplicationAlreadyExists extends RuntimeException {
    public ApplicationAlreadyExists() {
        super("Application already exists");
    }
}
