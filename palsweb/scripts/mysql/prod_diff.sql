alter table modeloutput add column usercomments_temp character varying(255);
update modeloutput set usercomments_temp = usercomments;
alter table modeloutput drop column usercomments;
alter table modeloutput add column usercomments text;
update modeloutput set usercomments = usercomments_temp;
alter table modeloutput drop column usercomments_temp;

CREATE TABLE experiment
(
  id integer NOT NULL,
  "name" character varying(255),
  shared boolean NOT NULL,
  owner_username character varying(255) NOT NULL,
  CONSTRAINT experiment_pkey PRIMARY KEY (id),
  CONSTRAINT fk71bbb81d6ed6d242 FOREIGN KEY (owner_username)
      REFERENCES palsuser (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE experiment OWNER TO pals;

CREATE TABLE experiment_palsuser
(
  experiments_id integer NOT NULL,
  sharedlist_username character varying(255) NOT NULL,
  CONSTRAINT experiment_palsuser_pkey PRIMARY KEY (experiments_id, sharedlist_username),
  CONSTRAINT fkc0d152516faec72 FOREIGN KEY (sharedlist_username)
      REFERENCES palsuser (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkc0d15252736166f FOREIGN KEY (experiments_id)
      REFERENCES experiment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE experiment_palsuser OWNER TO pals;

ALTER TABLE palsuser ADD currentexperiment_id integer;

ALTER TABLE palsuser ADD CONSTRAINT fk4062cb432199c596 FOREIGN KEY (currentexperiment_id)
      REFERENCES experiment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
CREATE TABLE experimentable
(
  id integer NOT NULL,
  experiment_id integer,
  CONSTRAINT experimentable_pkey PRIMARY KEY (id),
  CONSTRAINT fk3ee5f6f72736166f FOREIGN KEY (experiment_id)
      REFERENCES experiment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE experimentable OWNER TO pals;

INSERT INTO experimentable(id) SELECT id FROM analysable;

ALTER TABLE analysable ADD CONSTRAINT fk3e7cf2ac9683c747 FOREIGN KEY (id)
      REFERENCES experimentable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

select * into model_bak from model;

ALTER TABLE ONLY public.modeloutput DROP CONSTRAINT fk79cae56a30f3ca38 cascade;

drop table model;

CREATE TABLE model
(
  createddate timestamp without time zone,
  modelname character varying(255),
  ownerusername character varying(255),
  "version" character varying(255),
  id integer NOT NULL,
  CONSTRAINT model_pkey PRIMARY KEY (id),
  CONSTRAINT fk4710b092636c9e9 FOREIGN KEY (ownerusername)
      REFERENCES palsuser (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk4710b099683c747 FOREIGN KEY (id)
      REFERENCES experimentable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE model OWNER TO pals;

insert into experimentable(id) select modelid from model_bak;
insert into model(createddate,modelname,ownerusername,version,id) select createddate,modelname,ownerusername,version,modelid from model_bak;
drop table model_bak;

ALTER TABLE ONLY modeloutput
    ADD CONSTRAINT fk79cae56a30f3ca38 FOREIGN KEY (modelid) REFERENCES model(id);
    
update palsuser set password = md5(password);
