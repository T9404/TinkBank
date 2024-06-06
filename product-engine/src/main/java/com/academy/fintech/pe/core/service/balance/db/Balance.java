package com.academy.fintech.pe.core.service.balance.db;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "balance")
public class Balance {

    @Id
    @Column(name = "balance_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    private Agreement agreement;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "type")
    private String type;
}
