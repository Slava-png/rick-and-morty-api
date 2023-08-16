package springboot.rickandmorty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.rickandmorty.model.MovieCharacter;

import java.util.List;
import java.util.Set;

public interface MovieCharacterRepository extends JpaRepository<MovieCharacter, Long> {
    List<MovieCharacter> findMovieCharactersByExternalIdIn(Set<Long> ids);

    List<MovieCharacter> findMovieCharactersByNameContainingIgnoreCase(String pattern);
}
