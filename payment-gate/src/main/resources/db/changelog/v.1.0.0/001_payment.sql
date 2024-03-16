CREATE TABLE IF NOT EXISTS payment
(
    payment_id              UUID PRIMARY KEY,
    payment_amount          DECIMAL      NOT NULL,
    status                  VARCHAR(255) NOT NULL check (status in ('CREATED', 'PENDING', 'COMPLETED', 'FAILED')),
    sender_account_id       VARCHAR(255) NOT NULL,
    receiver_account_id     VARCHAR(255) NOT NULL,
    backoff_time            TIMESTAMP WITH TIME ZONE,
    attempted_backoff_count INT DEFAULT 0,
    merchant_id             UUID
);

COMMENT ON COLUMN payment.backoff_time IS 'The time when the payment should be requested again';
