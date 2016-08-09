ALTER TABLE consume_log DROP COLUMN project_id;
ALTER TABLE consume_log DROP COLUMN flavour_id;

DROP TABLE user_project;
DROP TABLE project;
DROP TABLE flavour;
