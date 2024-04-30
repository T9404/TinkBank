CREATE TABLE IF NOT EXISTS data_agreement
(
    data_agreement_id VARCHAR(255) PRIMARY KEY,
    agreement_number  VARCHAR(255)   NOT NULL,
    status            VARCHAR(255)   NOT NULL,
    business_date     DATE           NOT NULL,
    amount            DECIMAL(10, 2) NOT NULL
);

CREATE TRIGGER data_agreement_trigger
    BEFORE INSERT
    ON data_agreement
    FOR EACH ROW
EXECUTE PROCEDURE create_partition_and_insert();
