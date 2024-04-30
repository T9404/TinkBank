CREATE TABLE IF NOT EXISTS data_application
(
    data_application_id VARCHAR(255) PRIMARY KEY,
    application_id      VARCHAR(255) NOT NULL,
    status              VARCHAR(255) NOT NULL,
    business_date       DATE         NOT NULL
);

CREATE TRIGGER data_application_trigger
    BEFORE INSERT
    ON data_application
    FOR EACH ROW
EXECUTE PROCEDURE create_partition_and_insert();
