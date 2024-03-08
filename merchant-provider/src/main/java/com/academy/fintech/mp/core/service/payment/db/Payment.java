package com.academy.fintech.mp.core.service.payment.db;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private UUID paymentId;
    private String receiverAccountNumber;
    private String senderAccountNumber;
    private String currency;
    private BigDecimal requestedDisbursementAmount;
    private OffsetDateTime completionTime;
    private String status;
}
