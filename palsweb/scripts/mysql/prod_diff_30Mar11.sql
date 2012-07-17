alter table experimentable add column copiedfrom_id integer;
alter table experimentable add CONSTRAINT fk3ee5f6f76eec20b8 FOREIGN KEY (copiedfrom_id)
    REFERENCES experimentable (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;