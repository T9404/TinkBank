package com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_schedule")
public class PaymentSchedule {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    private Agreement agreement;

    @Column(name = "version", nullable = false)
    private int version;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
    }
}
