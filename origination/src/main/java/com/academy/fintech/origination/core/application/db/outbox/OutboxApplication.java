package com.academy.fintech.origination.core.application.db.outbox;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.outbox.enums.OutboxApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_application")
public class OutboxApplication {
    @Id
    @Column(name = "outbox_application_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID outboxApplicationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxApplicationStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

}
