package com.academy.fintech.pe.core.agreement.db.agreement;

import com.academy.fintech.pe.core.agreement.db.product.Product;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agreements")
public class Agreement {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", referencedColumnName = "code")
    private Product product;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "interest", nullable = false)
    private BigDecimal interest;

    @Column(name = "term", nullable = false)
    private int term;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    @Column(name = "origination_amount", nullable = false)
    private BigDecimal originationAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDateTime nextPaymentDate;

    @PrePersist
    public void prePersist() {
        id = product.getCode() + "-" + UUID.randomUUID();
    }
}
