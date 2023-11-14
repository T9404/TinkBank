CREATE TABLE IF NOT EXISTS product (
    code VARCHAR(255) PRIMARY KEY,
    min_term INT NOT NULL,
    max_term INT NOT NULL,
    min_principal_amount DECIMAL NOT NULL,
    max_principal_amount DECIMAL NOT NULL,
    min_interest DECIMAL NOT NULL,
    max_interest DECIMAL NOT NULL,
    min_origination_amount DECIMAL NOT NULL,
    max_origination_amount DECIMAL NOT NULL
);

INSERT INTO product (code, min_term, max_term, min_principal_amount, max_principal_amount, min_interest, max_interest, min_origination_amount, max_origination_amount)
VALUES ('CL 1.0', 3, 24, 50000, 500000, 8, 15, 2000, 10000);