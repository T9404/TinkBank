CREATE TABLE IF NOT EXISTS outbox_application
(
    outbox_application_id UUID PRIMARY KEY,
    application_id VARCHAR(255) REFERENCES applications(id),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(255) NOT NULL CHECK (status IN ('NEW', 'PENDING', 'SENT', 'ERROR')) DEFAULT 'NEW',
    retry_count INT NOT NULL DEFAULT 0
);
