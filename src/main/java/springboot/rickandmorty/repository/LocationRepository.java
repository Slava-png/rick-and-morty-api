package springboot.rickandmorty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.rickandmorty.model.Location;

import java.util.List;
import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findLocationsByExternalIdIn(Set<Long> ids);

    List<Location> findLocationsByNameIn(Set<String> names);

    List<Location> findLocationsByNameContainingIgnoreCase(String pattern);
}
