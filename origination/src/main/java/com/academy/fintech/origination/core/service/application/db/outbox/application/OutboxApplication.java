package com.academy.fintech.origination.core.service.application.db.outbox.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application applicationId;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}
