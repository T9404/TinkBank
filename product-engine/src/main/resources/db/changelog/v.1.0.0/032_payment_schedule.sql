CREATE TABLE IF NOT EXISTS payment_schedule
(
    id           VARCHAR(255) PRIMARY KEY,
    agreement_id VARCHAR(255) NOT NULL REFERENCES agreements (id),
    version      int          NOT NULL
);