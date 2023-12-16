package com.academy.fintech.origination.core.service.application.mapper;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.example.payment.AcceptingScoringRequest;

public final class AcceptingScoringRequestMapper {

    /**
     * Andrey allowed hard coding of values (in telegram) because we must get these values from api(client)
     */
    public static AcceptingScoringRequest mapToAcceptingScoringRequest(Application application) {
        return AcceptingScoringRequest.newBuilder()
                .setUserId(application.getUsers().getId())
                .setDisbursementAmount(application.getRequestedDisbursementAmount())
                .setSalary(application.getUsers().getSalary())
                .setLoanTerm(12)
                .setInterest(12)
                .setOriginationAmount(5000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .build();
    }
}
