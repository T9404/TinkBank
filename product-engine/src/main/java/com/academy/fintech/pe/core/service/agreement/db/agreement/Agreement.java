package com.academy.fintech.pe.core.service.agreement.db.agreement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private BigDecimal interest;

    @Column(nullable = false)
    private int term;

    @Column(nullable = false)
    private BigDecimal principalAmount;

    @Column(nullable = false)
    private BigDecimal originationAmount;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDateTime disbursementDate;

    @Column
    private LocalDateTime nextPaymentDate;
}
