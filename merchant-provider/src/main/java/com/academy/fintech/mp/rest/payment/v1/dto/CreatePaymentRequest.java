package com.academy.fintech.mp.rest.payment.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePaymentRequest(
        @JsonProperty("receiver_account_number")
        String receiverAccountNumber,

        @JsonProperty("sender_account_number")
        String senderAccountNumber,

        @JsonProperty("requested_disbursement_amount")
        BigDecimal requestedDisbursementAmount,

        String currency
) {
}
