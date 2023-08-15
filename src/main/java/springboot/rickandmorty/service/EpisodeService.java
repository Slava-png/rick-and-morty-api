package springboot.rickandmorty.service;

import springboot.rickandmorty.model.Episode;

import java.util.List;

public interface EpisodeService {
    void syncExternalEpisodes();

    Episode findById(Long id);

    List<Episode> findAll();
}
