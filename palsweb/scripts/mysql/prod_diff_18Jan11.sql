alter table datasetversion add column startdate timestamp without time zone;
alter table datasetversion add column enddate timestamp without time zone;
CREATE TABLE institution
(
  id integer NOT NULL,
  "name" character varying(255),
  CONSTRAINT institution_pkey PRIMARY KEY (id)
);
ALTER TABLE institution OWNER TO pals;
CREATE TABLE photo
(
  id integer NOT NULL,
  filename character varying(255),
  CONSTRAINT photo_pkey PRIMARY KEY (id)
);
ALTER TABLE photo OWNER TO pals;
alter table palsuser add column institution_id integer;
alter table palsuser add column photo_id integer;
alter table palsuser add column researchinterest text;
ALTER TABLE palsuser ADD CONSTRAINT fk4062cb439c055985 FOREIGN KEY (photo_id)
      REFERENCES photo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE palsuser ADD CONSTRAINT fk4062cb43b153af85 FOREIGN KEY (institution_id)
      REFERENCES institution (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;