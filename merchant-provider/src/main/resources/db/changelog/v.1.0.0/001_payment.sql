SET TIME ZONE 'UTC';

CREATE TABLE IF NOT EXISTS payment
(
    payment_id                    UUID PRIMARY KEY,
    receiver_account_number       VARCHAR(255)             NOT NULL,
    sender_account_number         VARCHAR(255)             NOT NULL,
    currency                      VARCHAR(3)               NOT NULL,
    requested_disbursement_amount DECIMAL                  NOT NULL,
    completion_time               TIMESTAMP WITH TIME ZONE NOT NULL,
    status                        VARCHAR(255)             NOT NULL check (status in ('PENDING', 'COMPLETED', 'FAILED'))
);
