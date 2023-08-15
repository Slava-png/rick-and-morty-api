--liquibase formatted sql
--changeset <slava>:<add-type-image-species-url-created-columns-to-movie-character.sql>
ALTER TABLE movie_character ADD IF NOT EXISTS species character varying(256) DEFAULT '';
ALTER TABLE movie_character ADD IF NOT EXISTS type character varying(256) DEFAULT '';
ALTER TABLE movie_character ADD IF NOT EXISTS image character varying(256) DEFAULT '';
ALTER TABLE movie_character ADD IF NOT EXISTS url character varying(256) DEFAULT '';
ALTER TABLE movie_character ADD IF NOT EXISTS created timestamp ;

--rollback DROP COLUMN species;
--rollback DROP COLUMN type;
--rollback DROP COLUMN image;
--rollback DROP COLUMN url;
--rollback DROP COLUMN created;