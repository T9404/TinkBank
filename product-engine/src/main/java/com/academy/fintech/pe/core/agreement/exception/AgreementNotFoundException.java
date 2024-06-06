package com.academy.fintech.pe.core.agreement.exception;

public class AgreementNotFoundException extends RuntimeException {
    public AgreementNotFoundException() {
        super("Agreement with this agreement number not found");
    }
}
