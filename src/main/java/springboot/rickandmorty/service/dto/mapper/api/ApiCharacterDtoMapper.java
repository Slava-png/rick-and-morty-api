package springboot.rickandmorty.service.dto.mapper.api;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.*;
import springboot.rickandmorty.model.dto.api.ApiCharacterDto;
import springboot.rickandmorty.repository.EpisodeRepository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ApiCharacterDtoMapper {
    private final EpisodeRepository episodeRepository;

    public MovieCharacter toModel(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setExternalId(dto.getId());
        movieCharacter.setName(dto.getName());
        movieCharacter.setSpecies(dto.getSpecies());
        movieCharacter.setType(dto.getType());
//        movieCharacter.setEpisodes(getEpisodesWithExternalIds(dto.getEpisode()));
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setUrl(dto.getUrl());
        movieCharacter.setImage(dto.getImage());
        movieCharacter.setCreated(dto.getCreated());

        return movieCharacter;
    }

    private List<Episode> getEpisodesWithExternalIds(List<String> episodes) {
        return episodes.stream()
                .map(url -> {
                    String[] parts = url.split("/");
                    Long externalId = Long.parseLong(parts[parts.length - 1]);
                    Episode episode = episodeRepository.findEpisodeByExternalId(externalId);
                    if (episode != null) {
                        return episode;
                    }
                    return new Episode(externalId);
                })
                .collect(Collectors.toList());
    }
}
