package com.academy.fintech.origination.core.service.application.db.outbox.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxApplicationRepository extends JpaRepository<OutboxApplication, UUID> {
}
