--liquibase formatted sql
--changeset <slava>:<create-episode-table>
CREATE TABLE IF NOT EXISTS public.episode (
       id bigint NOT NULL,
       external_id bigint NOT NULL,
       name character varying(256) NOT NULL,
       air_date character varying(256) NOT NULL,
       episode character varying(256) NOT NULL,
       url character varying(256) NOT NULL,
       created timestamp NOT NULL,
       CONSTRAINT episode_pk PRIMARY KEY (id)
);

--rollback DROP TABLE episode;