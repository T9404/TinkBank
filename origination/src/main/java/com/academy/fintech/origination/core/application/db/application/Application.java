package com.academy.fintech.origination.core.application.db.application;

import com.academy.fintech.origination.core.users.db.Users;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @Column(name = "requested_disbursement_amount", nullable = false)
    private int requestedDisbursementAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "agreement_id")
    private String agreementId;

    @PrePersist
    public void prePersist() {
        id = String.valueOf(UUID.randomUUID());
    }
}
