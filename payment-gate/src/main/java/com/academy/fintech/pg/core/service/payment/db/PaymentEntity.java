package com.academy.fintech.pg.core.service.payment.db;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "sender_account_id")
    private String senderAccountId;

    @Column(name = "receiver_account_id")
    private String receiverAccountId;

    @Column(name = "backoff_time")
    private OffsetDateTime backoffTime;

    @Column(name = "attempted_backoff_count")
    private int attemptedBackoffCount;

    @Column(name = "merchant_id")
    private UUID merchantId;
}
