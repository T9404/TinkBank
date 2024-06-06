package com.academy.fintech.pe.core.agreement.validation;

import com.academy.fintech.pe.core.agreement.exception.PaymentInFutureException;
import proto.DisbursementRequest;

import java.time.LocalDateTime;

import static com.academy.fintech.pe.core.converter.TimestampConverter.fromGoogleTimestampToLocalDateTime;

public final class AgreementDisbursementValidator {

    public static void checkDisbursementValidity(DisbursementRequest request) {
        checkDisbursementDate(request);
    }

    private static void checkDisbursementDate(DisbursementRequest request) {
        LocalDateTime disbursementDate = fromGoogleTimestampToLocalDateTime(request.getPaymentDate());
        if (disbursementDate.isAfter(LocalDateTime.now())) {
            throw new PaymentInFutureException();
        }
    }
}
