alter table palsuser add column showemail boolean;
alter table experiment add column sharewithall boolean not null default false;

CREATE TABLE filedata
(
  id integer NOT NULL,
  data oid,
  CONSTRAINT filedata_pkey PRIMARY KEY (id)
);
ALTER TABLE filedata OWNER TO pals;

CREATE TABLE palsfile
(
  id integer NOT NULL,
  contenttype character varying(255),
  "name" character varying(255),
  data_id integer,
  CONSTRAINT palsfile_pkey PRIMARY KEY (id),
  CONSTRAINT fk405bd4f4732a982b FOREIGN KEY (data_id)
      REFERENCES filedata (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE palsfile OWNER TO pals;

CREATE TABLE experimentable_palsfile
(
  experimentable_id integer NOT NULL,
  files_id integer NOT NULL,
  CONSTRAINT fk3e0bf53c36ff7daf FOREIGN KEY (experimentable_id)
      REFERENCES experimentable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk3e0bf53c59bbf3cc FOREIGN KEY (files_id)
      REFERENCES palsfile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE experimentable_palsfile OWNER TO pals;

alter table model add column commentsm text;
alter table model add column referencesm text;
alter table model add column urlm character varying(255);
