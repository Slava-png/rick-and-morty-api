package springboot.rickandmorty.service.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.MovieCharacter;
import springboot.rickandmorty.model.dto.MovieCharacterResponseDto;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MovieCharacterDtoMapper {
    private final LocationDtoMapper locationDtoMapper;
    private final EpisodeDtoMapper episodeDtoMapper;

    public MovieCharacterResponseDto toDto(MovieCharacter model) {
        MovieCharacterResponseDto dto = new MovieCharacterResponseDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setGender(model.getGender().name());
        dto.setStatus(model.getStatus().name());
        dto.setSpecies(model.getSpecies());
        dto.setType(model.getType());
        dto.setImage(model.getImage());
        dto.setUrl(model.getUrl());
        dto.setCreated(model.getCreated());
        if (model.getLocation() != null) {
            dto.setLocation(locationDtoMapper.toDto(model.getLocation()));
        }
        if (model.getOrigin() != null) {
            dto.setOrigin(locationDtoMapper.toDto(model.getOrigin()));
        }
        dto.setEpisodes(model.getEpisodes().stream()
                .map(episodeDtoMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }
}
