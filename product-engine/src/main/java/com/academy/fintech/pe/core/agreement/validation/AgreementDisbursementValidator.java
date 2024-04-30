package com.academy.fintech.pe.core.agreement.validation;

import com.academy.fintech.pe.core.converter.ProtobufConverter;
import com.academy.fintech.pe.core.agreement.exception.PaymentInFutureException;
import proto.DisbursementRequest;

import java.time.LocalDateTime;

public final class AgreementDisbursementValidator {

    public static void checkDisbursementValidity(DisbursementRequest request) {
        checkDisbursementDate(request);
    }

    private static void checkDisbursementDate(DisbursementRequest request) {
        LocalDateTime disbursementDate = ProtobufConverter.fromGoogleTimestampToLocalDateTime(request.getPaymentDate());
        if (disbursementDate.isAfter(LocalDateTime.now())) {
            throw new PaymentInFutureException();
        }
    }
}
