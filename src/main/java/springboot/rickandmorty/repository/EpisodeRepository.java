package springboot.rickandmorty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.rickandmorty.model.Episode;

import java.util.List;
import java.util.Set;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findEpisodesByExternalIdIn(Set<Long> ids);

    Episode findEpisodeByExternalId(Long externalId);
}
