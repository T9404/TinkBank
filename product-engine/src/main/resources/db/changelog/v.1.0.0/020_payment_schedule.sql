CREATE TABLE IF NOT EXISTS payment_schedule (
    id UUID PRIMARY KEY,
    agreement_number UUID NOT NULL REFERENCES agreement(id),
    version int NOT NULL
);