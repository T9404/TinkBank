package com.academy.fintech.pe.core.service.agreement.exception;

public class PaymentInFutureException extends RuntimeException {
    public PaymentInFutureException() {
        super("Payment cannot be in the future");
    }
}
