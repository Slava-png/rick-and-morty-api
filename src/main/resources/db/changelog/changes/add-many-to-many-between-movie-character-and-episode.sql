--liquibase formatted sql
--changeset <slava>:<add-many-to-many-between-movie-character-and-episode.sql>
CREATE TABLE IF NOT EXISTS movie_character_episode (
    movie_character_id bigint NOT NULL REFERENCES movie_character (id),
    episode_id bigint NOT NULL REFERENCES episode (id),
    CONSTRAINT movie_character_episode_pk PRIMARY KEY (movie_character_id, episode_id)
)

--rollback DROP TABLE movie_character_episode;