package springboot.rickandmorty.service.dto.mapper.api;

import org.springframework.stereotype.Component;
import springboot.rickandmorty.model.Location;
import springboot.rickandmorty.model.dto.api.ApiLocationDto;

@Component
public class ApiLocationDtoMapper {
    public Location toModel(ApiLocationDto dto) {
        Location location = new Location();
        location.setExternalId(dto.getId());
        location.setUrl(dto.getUrl());
        location.setName(dto.getName());
        location.setCreated(dto.getCreated());
        return location;
    }
}
