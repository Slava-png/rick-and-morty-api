package springboot.rickandmorty.service.dto.mapper;

import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.Episode;
import springboot.rickandmorty.model.dto.EpisodeResponseDto;

@Component
public class EpisodeDtoMapper {
    public EpisodeResponseDto toDto(Episode episode) {
        EpisodeResponseDto dto = new EpisodeResponseDto();
        dto.setId(episode.getId());
        dto.setName(episode.getName());
        dto.setAirDate(episode.getAirDate());
        dto.setEpisode(episode.getEpisode());
        dto.setUrl(episode.getUrl());
        dto.setCreated(episode.getCreated());
        return dto;
    }
}
