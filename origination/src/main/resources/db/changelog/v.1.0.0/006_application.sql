CREATE TABLE IF NOT EXISTS applications (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) REFERENCES users(id),
    requested_disbursement_amount INTEGER NOT NULL CHECK ( requested_disbursement_amount > 0 ),
    status VARCHAR(255) NOT NULL CHECK ( status IN ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED') )
);