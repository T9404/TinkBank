CREATE OR REPLACE FUNCTION create_partition_and_insert() RETURNS trigger AS
$BODY$
DECLARE
    partition_date TEXT;
    partition      TEXT;
BEGIN
    partition_date := to_char(NEW.business_date, 'YYYY_MM_DD');
    partition := TG_TABLE_NAME || '_' || partition_date;
    IF NOT EXISTS(SELECT relname FROM pg_class WHERE relname = partition) THEN
        RAISE NOTICE 'A partition has been created %',partition;
        EXECUTE
            'CREATE TABLE ' || partition || ' (check (business_date = ''' || NEW.business_date || ''')) INHERITS (' ||
            TG_TABLE_NAME || ');';
    END IF;
    EXECUTE 'INSERT INTO ' || partition || ' SELECT(' || TG_RELNAME || ' ' || quote_literal(NEW) || ').*;';
    RETURN NULL;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100;
