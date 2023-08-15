package springboot.rickandmorty.service;

import springboot.rickandmorty.model.MovieCharacter;

import java.util.List;

public interface MovieCharacterService {
    void syncExternalCharacters();

    MovieCharacter getRandomly();

    List<MovieCharacter> findByNameInPattern(String pattern);

    MovieCharacter findById(Long id);

    List<MovieCharacter> findAll();
}
