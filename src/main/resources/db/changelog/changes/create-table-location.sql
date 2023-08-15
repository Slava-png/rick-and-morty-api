--liquibase formatted sql
--changeset <slava>:<create-location-table>
CREATE TABLE IF NOT EXISTS public.location (
       id bigint NOT NULL,
       external_id bigint NOT NULL,
       name character varying(256) NOT NULL,
       url character varying(256) NOT NULL,
       created timestamp NOT NULL,
       CONSTRAINT location_pk PRIMARY KEY (id)
);

--rollback DROP TABLE location;