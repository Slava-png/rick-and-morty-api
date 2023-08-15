package springboot.rickandmorty.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springboot.rickandmorty.exception.NoEntityFoundException;
import springboot.rickandmorty.model.Location;
import springboot.rickandmorty.model.dto.api.ApiLocationDto;
import springboot.rickandmorty.model.dto.api.ApiLocationResponseDto;
import springboot.rickandmorty.repository.LocationRepository;
import springboot.rickandmorty.service.LocationService;
import springboot.rickandmorty.service.dto.mapper.api.ApiLocationDtoMapper;
import springboot.rickandmorty.util.HttpClient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {
    private static final String URL = "https://rickandmortyapi.com/api/location";
    private final LocationRepository locationRepository;
    private final ApiLocationDtoMapper apiLocationDtoMapper;
    private final HttpClient httpClient;

    @Scheduled(cron = "${location.cron.value}")
    @Override
    public void syncExternalLocations() {
        ApiLocationResponseDto apiResponseDto = httpClient.get(URL,
                ApiLocationResponseDto.class);
        saveAndUpdateLocations(apiResponseDto);

        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiLocationResponseDto.class);
            saveAndUpdateLocations(apiResponseDto);
        }
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> {
            throw new NoEntityFoundException("Location with id: "
                    + id + " doesn't exist");
        });
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    private void saveAndUpdateLocations(ApiLocationResponseDto apiResponseDto) {
        Map<Long, ApiLocationDto> fetchedDtos = apiResponseDto.getResults().stream()
                .collect(Collectors.toMap(ApiLocationDto::getId, Function.identity()));
        Set<Long> externalIds = fetchedDtos.keySet();

        List<Location> locationsFromDbList = locationRepository
                .findLocationsByExternalIdIn(externalIds);

        Map<Long, Location> existingLocationssMap = locationsFromDbList.stream()
                .collect(Collectors.toMap(Location::getExternalId, Function.identity()));

        // separate dtos for saving and updating
        externalIds.removeAll(existingLocationssMap.keySet());

        // save all new locations
        List<Location> locationsForInserting = externalIds.stream()
                .map(i -> apiLocationDtoMapper.toModel(fetchedDtos.get(i)))
                .collect(Collectors.toList());
        locationRepository.saveAll(locationsForInserting);

        // update all existing locations
        Set<Long> externalIdsOfElementsForUpdating = fetchedDtos.keySet();
        externalIdsOfElementsForUpdating.removeAll(externalIds);

        List<Location> locationsForUpdating = externalIdsOfElementsForUpdating.stream()
                .map(i -> apiLocationDtoMapper.toModel(fetchedDtos.get(i)))
                .map(e -> {
                    Location location = existingLocationssMap.get(e.getExternalId());
                    e.setId(location.getId());
                    return e;
                })
                .collect(Collectors.toList());
        locationRepository.saveAll(locationsForUpdating);
    }
}
