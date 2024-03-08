package com.academy.fintech.mp.public_interface.payment.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreatePaymentResponse(
        @JsonProperty("payment_id")
        UUID paymentId,

        String status
) {
}
