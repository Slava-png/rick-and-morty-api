package springboot.rickandmorty.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.rickandmorty.model.dto.LocationResponseDto;
import springboot.rickandmorty.service.LocationService;
import springboot.rickandmorty.service.dto.mapper.LocationDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;
    private final LocationDtoMapper locationDtoMapper;

    @GetMapping("/{id}")
    public LocationResponseDto get(@PathVariable Long id) {
        return locationDtoMapper.toDto(locationService.findById(id));
    }

    @GetMapping("/all")
    public List<LocationResponseDto> getAll() {
        return locationService.findAll().stream()
                .map(locationDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
