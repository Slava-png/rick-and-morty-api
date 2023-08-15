--liquibase formatted sql
--changeset <slava>:<create-episode-sequence-id>
CREATE SEQUENCE IF NOT EXISTS public.episode_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.episode_id_seq;