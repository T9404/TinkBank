package com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GetPaymentRequest(
        @JsonProperty("payment_id")
        UUID paymentId
) {
}
