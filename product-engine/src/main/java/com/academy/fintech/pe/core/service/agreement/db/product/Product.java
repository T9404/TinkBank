package com.academy.fintech.pe.core.service.agreement.db.product;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "min_term", nullable = false)
    private int minTerm;

    @Column(name = "max_term", nullable = false)
    private int maxTerm;

    @Column(name = "min_principal_amount", nullable = false)
    private BigDecimal minPrincipalAmount;

    @Column(name = "max_principal_amount", nullable = false)
    private BigDecimal maxPrincipalAmount;

    @Column(name = "min_interest", nullable = false)
    private BigDecimal minInterest;

    @Column(name = "max_interest", nullable = false)
    private BigDecimal maxInterest;

    @Column(name = "min_origination_amount", nullable = false)
    private BigDecimal minOriginationAmount;

    @Column(name = "max_origination_amount", nullable = false)
    private BigDecimal maxOriginationAmount;
}
