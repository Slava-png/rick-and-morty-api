package springboot.rickandmorty.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springboot.rickandmorty.exception.NoEntityFoundException;
import springboot.rickandmorty.model.Episode;
import springboot.rickandmorty.model.Location;
import springboot.rickandmorty.model.MovieCharacter;
import springboot.rickandmorty.model.dto.api.ApiCharacterDto;
import springboot.rickandmorty.model.dto.api.ApiCharacterResponseDto;
import springboot.rickandmorty.repository.EpisodeRepository;
import springboot.rickandmorty.repository.LocationRepository;
import springboot.rickandmorty.repository.MovieCharacterRepository;
import springboot.rickandmorty.service.MovieCharacterService;
import springboot.rickandmorty.service.dto.mapper.api.ApiCharacterDtoMapper;
import springboot.rickandmorty.util.HttpClient;

@AllArgsConstructor
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private static final String URL = "https://rickandmortyapi.com/api/character";
    private final MovieCharacterRepository movieCharacterRepository;
    private final HttpClient httpClient;
    private final ApiCharacterDtoMapper apiCharacterDtoMapper;
    private final LocationRepository locationRepository;
    private final EpisodeRepository episodeRepository;
    private static final Logger logger = LogManager.getLogger(MovieCharacterServiceImpl.class);

    @Scheduled(cron = "${movie_character.cron.value}")
    @Override
    public void syncExternalCharacters() {
        ApiCharacterResponseDto apiResponseDto = httpClient.get(URL, ApiCharacterResponseDto.class);
        saveAndUpdateCharacters(apiResponseDto);

        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiCharacterResponseDto.class);
            saveAndUpdateCharacters(apiResponseDto);
        }
        logger.info("Movie characters were integrated with Rick and Morty api");
    }

    @Override
    public MovieCharacter getRandomly() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);

        return movieCharacterRepository.findById(randomId).orElseThrow(() -> {
            logger.error("Movie character with id: " + randomId + " doesn't exist");
            throw new NoEntityFoundException("Movie character with id: "
                    + randomId + " doesn't exist");
        });
    }

    @Override
    public List<MovieCharacter> findByNameInPattern(String pattern) {
        return movieCharacterRepository.findMovieCharactersByNameContainingIgnoreCase(pattern);
    }

    @Override
    public MovieCharacter findById(Long id) {
        return movieCharacterRepository.findById(id).orElseThrow(() -> {
            throw new NoEntityFoundException("Movie character with id: "
                    + id + " doesn't exist");
        });
    }

    @Override
    public List<MovieCharacter> findAll() {
        return movieCharacterRepository.findAll();
    }

    private void saveAndUpdateCharacters(ApiCharacterResponseDto apiResponseDto) {
        Map<Long, ApiCharacterDto> fetchedDtos = apiResponseDto.getResults().stream()
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));
        Set<Long> externalIds = fetchedDtos.keySet();

        List<MovieCharacter> charactersFromDbList = movieCharacterRepository
                .findMovieCharactersByExternalIdIn(externalIds);

        Map<Long, MovieCharacter> existingCharactersMap = charactersFromDbList.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        // separate dtos for saving and updating
        externalIds.removeAll(existingCharactersMap.keySet());

        // parse dtos to models
        List<MovieCharacter> charactersForInserting = externalIds.stream()
                .map(i -> apiCharacterDtoMapper.toModel(fetchedDtos.get(i)))
                .collect(Collectors.toList());

        // create relations between movie characters and locations
        List<MovieCharacter> characterWithLocationsForInsertion =
                createRelationsWithLocations(charactersForInserting, apiResponseDto, fetchedDtos);

        // create relations between movie characters and episodes
        List<MovieCharacter> characterWithEpisodesForInsertion =
                createRelationsWithEpisodes(characterWithLocationsForInsertion, apiResponseDto);
        movieCharacterRepository.saveAll(characterWithEpisodesForInsertion);

        // update all existing characters
        Set<Long> externalIdsOfElementsForUpdating = fetchedDtos.keySet();
        externalIdsOfElementsForUpdating.removeAll(externalIds);

        // parse characters for update (with setting id from DB)
        List<MovieCharacter> charactersForUpdating = externalIdsOfElementsForUpdating.stream()
                .map(i -> apiCharacterDtoMapper.toModel(fetchedDtos.get(i)))
                .peek(e -> {
                    MovieCharacter character = existingCharactersMap.get(e.getExternalId());
                    e.setId(character.getId());
                })
                .collect(Collectors.toList());

        // create relations between movie characters and locations
        List<MovieCharacter> charactersWithLocationsForUpdate =
                createRelationsWithLocations(charactersForUpdating, apiResponseDto, fetchedDtos);

        // create relations between movie characters and episodes
        List<MovieCharacter> characterWithEpisodesForUpdate =
                createRelationsWithEpisodes(charactersWithLocationsForUpdate, apiResponseDto);
        movieCharacterRepository.saveAll(characterWithEpisodesForUpdate);
    }

    private List<MovieCharacter> createRelationsWithLocations(List<MovieCharacter> characters,
                                                 ApiCharacterResponseDto apiResponseDto, Map<Long,
                                                    ApiCharacterDto> fetchedDtos) {
        // get required locations
        Set<String> locationNames = apiResponseDto.getResults().stream()
                .map(e -> e.getLocation().getName())
                .collect(Collectors.toSet());
        Map<String, Location> locationWithNamesMap = locationRepository
                .findLocationsByNameIn(locationNames).stream()
                .collect(Collectors.toMap(Location::getName, Function.identity()));

        // get required origins
        Set<String> originNames = apiResponseDto.getResults().stream()
                .map(e -> e.getOrigin().getName())
                .collect(Collectors.toSet());
        Map<String, Location> originWithNamesMap = locationRepository
                .findLocationsByNameIn(originNames).stream()
                .collect(Collectors.toMap(Location::getName, Function.identity()));

        // connect movie characters with locations
        return characters.stream()
                .peek(character -> {
                    String locationName = fetchedDtos.get(character.getExternalId()).getLocation().getName();
                    Location location = locationWithNamesMap.get(locationName);
                    character.setLocation(location);
                }).peek(character -> {
                    String originName = fetchedDtos.get(character.getExternalId()).getOrigin().getName();
                    Location origin = originWithNamesMap.get(originName);
                    character.setOrigin(origin);
                }).collect(Collectors.toList());
    }

    private List<MovieCharacter> createRelationsWithEpisodes(List<MovieCharacter> characters,
                                                        ApiCharacterResponseDto apiResponseDto) {
        Map<Long, List<Long>> charactersExternalIdsWithEpisodesExternalIdsMap = new HashMap<>();
        for (ApiCharacterDto character: apiResponseDto.getResults()) {
            List<Long> episodes = character.getEpisode().stream()
                    .map(s -> s.split("/"))
                    .map(s -> Long.parseLong(s[s.length - 1]))
                    .collect(Collectors.toList());

            charactersExternalIdsWithEpisodesExternalIdsMap.put(character.getId(), episodes);
        }

        // get all external ids for episodes
        Set<Long> allEpisodesExternalIds = charactersExternalIdsWithEpisodesExternalIdsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        List<Episode> episodesByExternalIdIn =
                episodeRepository.findEpisodesByExternalIdIn(allEpisodesExternalIds);

        Map<Long, Episode> episodesWithExternalIdsMap = episodesByExternalIdIn.stream()
                .collect(Collectors.toMap(Episode::getExternalId, Function.identity()));

        Map<Long, List<Episode>> characterExternalIdsWithEpisodesMap = new HashMap<>();

        // populate map: characterExternalIdsWithEpisodesMap
        for (Long characterId: charactersExternalIdsWithEpisodesExternalIdsMap.keySet()) {
            List<Long> episodesExternalIds =
                    charactersExternalIdsWithEpisodesExternalIdsMap.get(characterId);
            List<Episode> episodesForSpecificCharacter = episodesExternalIds.stream()
                    .map(episodesWithExternalIdsMap::get)
                    .collect(Collectors.toList());
            characterExternalIdsWithEpisodesMap.put(characterId, episodesForSpecificCharacter);
        }

        // create relations between movie character and episode
        for (MovieCharacter character: characters) {
            character.setEpisodes(characterExternalIdsWithEpisodesMap.get(character.getExternalId()));
        }
        return characters;
    }
}
