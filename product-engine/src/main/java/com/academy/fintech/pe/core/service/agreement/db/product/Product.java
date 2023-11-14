package com.academy.fintech.pe.core.service.agreement.db.product;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private String code;

    @Column(nullable = false)
    private int minTerm;

    @Column(nullable = false)
    private int maxTerm;

    @Column(nullable = false)
    private BigDecimal minPrincipalAmount;

    @Column(nullable = false)
    private BigDecimal maxPrincipalAmount;

    @Column(nullable = false)
    private BigDecimal minInterest;

    @Column(nullable = false)
    private BigDecimal maxInterest;

    @Column(nullable = false)
    private BigDecimal minOriginationAmount;

    @Column(nullable = false)
    private BigDecimal maxOriginationAmount;
}
