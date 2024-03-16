package com.academy.fintech.pg.rest.payment.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePaymentRequest(

        @JsonProperty("sender_account_number")
        String senderAccountNumber,

        @JsonProperty("receiver_account_number")
        String receiverAccountNumber,

        @JsonProperty("requested_disbursement_amount")
        BigDecimal requestedDisbursementAmount,

        String currency
) {
}
