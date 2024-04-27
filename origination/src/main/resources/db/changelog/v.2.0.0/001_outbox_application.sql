CREATE TABLE IF NOT EXISTS outbox_application
(
    outbox_application_id UUID PRIMARY KEY,
    application_id VARCHAR(255) REFERENCES applications(id),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
