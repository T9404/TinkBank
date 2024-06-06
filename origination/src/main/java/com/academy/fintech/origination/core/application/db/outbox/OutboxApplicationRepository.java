package com.academy.fintech.origination.core.application.db.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxApplicationRepository extends JpaRepository<OutboxApplication, UUID> {

    @Query(value = """
        UPDATE outbox_application
        SET status = 'PENDING', retry_count = retry_count + 1, updated_at = CURRENT_TIMESTAMP
        WHERE outbox_application_id IN (
            SELECT outbox_application_id
            FROM outbox_application
            WHERE status = :status
            ORDER BY updated_at
            FOR UPDATE SKIP LOCKED
            LIMIT 1000
        )
        RETURNING outbox_application_id, application_id, updated_at, status, retry_count;
    """, nativeQuery = true)
    List<OutboxApplication> setAllPending(String status);

    @Query(value = """
        SELECT outbox_application_id, application_id, updated_at, status, retry_count
        FROM outbox_application
        WHERE status = :status
        FOR UPDATE SKIP LOCKED
        LIMIT 1000
    """, nativeQuery = true)
    List<OutboxApplication> findAllByStatus(String status);
}
