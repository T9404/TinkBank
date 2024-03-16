CREATE TABLE IF NOT EXISTS balance
(
    balance_id   UUID PRIMARY KEY,
    agreement_id VARCHAR(255) NOT NULL REFERENCES agreements (id),
    balance      DECIMAL      NOT NULL,
    type         VARCHAR(6)   NOT NULL check (type in ('CREDIT', 'DEBIT'))
);
