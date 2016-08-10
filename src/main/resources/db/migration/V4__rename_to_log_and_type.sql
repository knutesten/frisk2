ALTER TABLE frisk_consume_type RENAME TO type;

ALTER TABLE consume_log RENAME COLUMN consume_type_id TO type_id;
ALTER TABLE consume_log RENAME TO log;
