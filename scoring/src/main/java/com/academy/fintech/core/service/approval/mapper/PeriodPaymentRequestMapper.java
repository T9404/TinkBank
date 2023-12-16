package com.academy.fintech.core.service.approval.mapper;

import com.example.payment.AcceptingScoringRequest;
import com.example.payment.PeriodPaymentRequest;

public class PeriodPaymentRequestMapper {

    public static PeriodPaymentRequest mapToPeriodPaymentRequest(AcceptingScoringRequest request) {
        return PeriodPaymentRequest.newBuilder()
                .setUserId(request.getUserId())
                .setLoanTerm(request.getLoanTerm())
                .setDisbursementAmount(request.getDisbursementAmount())
                .setOriginationAmount(request.getOriginationAmount())
                .setInterest(request.getInterest())
                .setProductCode(request.getProductCode())
                .setProductVersion(request.getProductVersion())
                .build();
    }
}
