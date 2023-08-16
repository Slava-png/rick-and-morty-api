package springboot.rickandmorty.service.impl;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springboot.rickandmorty.exception.NoEntityFoundException;
import springboot.rickandmorty.model.Episode;
import springboot.rickandmorty.model.dto.api.ApiEpisodeDto;
import springboot.rickandmorty.model.dto.api.ApiEpisodeResponseDto;
import springboot.rickandmorty.repository.EpisodeRepository;
import springboot.rickandmorty.service.EpisodeService;
import springboot.rickandmorty.service.dto.mapper.api.ApiEpisodeDtoMapper;
import springboot.rickandmorty.util.HttpClient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EpisodeServiceImpl implements EpisodeService {
    private static final String URL = "https://rickandmortyapi.com/api/episode";
    private final EpisodeRepository episodeRepository;
    private final ApiEpisodeDtoMapper apiEpisodeDtoMapper;
    private final HttpClient httpClient;
    private static final Logger logger = LogManager.getLogger(EpisodeServiceImpl.class);

    @Scheduled(cron = "${episode.cron.value}")
    @Override
    public void syncExternalEpisodes() {
        ApiEpisodeResponseDto apiResponseDto = httpClient.get(URL,
                ApiEpisodeResponseDto.class);
        saveAndUpdateEpisodes(apiResponseDto);

        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiEpisodeResponseDto.class);
            saveAndUpdateEpisodes(apiResponseDto);
        }
        logger.info("Episodes were integrated with Rick and Morty api");
    }

    @Override
    public Episode findById(Long id) {
        return episodeRepository.findById(id).orElseThrow(() -> {
            logger.error("Episode with id: " + id + " doesn't exist");
            throw new NoEntityFoundException("Episode with id: "
                    + id + " doesn't exist");
        });
    }

    @Override
    public List<Episode> findAll() {
        return episodeRepository.findAll();
    }

    private void saveAndUpdateEpisodes(ApiEpisodeResponseDto apiResponseDto) {
        Map<Long, ApiEpisodeDto> fetchedDtos = apiResponseDto.getResults().stream()
                .collect(Collectors.toMap(ApiEpisodeDto::getId, Function.identity()));
        Set<Long> externalIds = fetchedDtos.keySet();

        List<Episode> charactersFromDbList = episodeRepository
                .findEpisodesByExternalIdIn(externalIds);

        Map<Long, Episode> existingCharactersMap = charactersFromDbList.stream()
                .collect(Collectors.toMap(Episode::getExternalId, Function.identity()));

        // separate dtos for saving and updating
        externalIds.removeAll(existingCharactersMap.keySet());

        // save all new characters
        List<Episode> episodesForInserting = externalIds.stream()
                .map(i -> apiEpisodeDtoMapper.toModel(fetchedDtos.get(i)))
                .collect(Collectors.toList());
        episodeRepository.saveAll(episodesForInserting);

        // update all existing characters
        Set<Long> externalIdsOfElementsForUpdating = fetchedDtos.keySet();
        externalIdsOfElementsForUpdating.removeAll(externalIds);

        List<Episode> charactersForUpdating = externalIdsOfElementsForUpdating.stream()
                .map(i -> apiEpisodeDtoMapper.toModel(fetchedDtos.get(i)))
                .peek(e -> {
                    Episode episode = existingCharactersMap.get(e.getExternalId());
                    e.setId(episode.getId());
                })
                .collect(Collectors.toList());
        episodeRepository.saveAll(charactersForUpdating);
    }
}
