CREATE TABLE IF NOT EXISTS agreement (
    id UUID PRIMARY KEY,
    product_code VARCHAR NOT NULL REFERENCES product(code),
    client_id UUID NOT NULL,
    interest DECIMAL NOT NULL,
    term INT NOT NULL,
    principal_amount DECIMAL NOT NULL,
    origination_amount DECIMAL NOT NULL,
    status VARCHAR(255) NOT NULL CHECK (status IN ('NEW', 'ACTIVE', 'CLOSED')),
    disbursement_date TIMESTAMP,
    next_payment_date TIMESTAMP
);