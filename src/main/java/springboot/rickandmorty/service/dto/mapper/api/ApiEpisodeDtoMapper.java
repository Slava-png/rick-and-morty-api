package springboot.rickandmorty.service.dto.mapper.api;

import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.Episode;
import springboot.rickandmorty.model.dto.api.ApiEpisodeDto;

@Component
public class ApiEpisodeDtoMapper {
    public Episode toModel(ApiEpisodeDto dto) {
        Episode episode = new Episode();
        episode.setExternalId(dto.getId());
        episode.setName(dto.getName());
        episode.setAirDate(dto.getAir_date());
        episode.setEpisode(dto.getEpisode());
        episode.setUrl(dto.getUrl());
        episode.setCreated(dto.getCreated());
        return episode;
    }
}
