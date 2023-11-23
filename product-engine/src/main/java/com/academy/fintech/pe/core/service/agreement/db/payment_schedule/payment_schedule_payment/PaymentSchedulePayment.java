package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
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
@Table(name = "payment_schedule_payment")
public class PaymentSchedulePayment {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_schedule_id", referencedColumnName = "id")
    private PaymentSchedule paymentSchedule;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "period_payment", nullable = false)
    private BigDecimal periodPayment;

    @Column(name = "interest_payment", nullable = false)
    private BigDecimal interestPayment;

    @Column(name = "principal_payment", nullable = false)
    private BigDecimal principalPayment;

    @Column(name = "period_number", nullable = false)
    private int periodNumber;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
    }
}
