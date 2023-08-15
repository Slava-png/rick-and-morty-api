package springboot.rickandmorty.service.dto.mapper;

import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.Location;
import springboot.rickandmorty.model.dto.LocationResponseDto;

@Component
public class LocationDtoMapper {
    public LocationResponseDto toDto(Location location) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setUrl(location.getUrl());
        dto.setCreated(location.getCreated());
        return dto;
    }
}
