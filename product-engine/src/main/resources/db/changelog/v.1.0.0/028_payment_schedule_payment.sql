CREATE TABLE IF NOT EXISTS payment_schedule_payment (
    id VARCHAR(255) PRIMARY KEY,
    payment_schedule_id VARCHAR(255) NOT NULL REFERENCES payment_schedule(id),
    status VARCHAR(255) NOT NULL CHECK (status IN ('PAID', 'OVERDUE', 'FUTURE')),
    payment_date TIMESTAMP NOT NULL,
    period_payment DECIMAL NOT NULL,
    interest_payment DECIMAL NOT NULL,
    principal_payment DECIMAL NOT NULL,
    period_number INT NOT NULL
);