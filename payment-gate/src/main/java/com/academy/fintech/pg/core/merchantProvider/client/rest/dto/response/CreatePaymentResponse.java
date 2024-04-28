package com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatePaymentResponse(
        @JsonProperty("payment_id")
        UUID paymentId,

        String status
) {
}
