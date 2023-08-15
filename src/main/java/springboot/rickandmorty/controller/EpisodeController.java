package springboot.rickandmorty.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.rickandmorty.model.dto.EpisodeResponseDto;
import springboot.rickandmorty.service.EpisodeService;
import springboot.rickandmorty.service.dto.mapper.EpisodeDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;
    private final EpisodeDtoMapper episodeDtoMapper;

    @GetMapping("/{id}")
    public EpisodeResponseDto get(@PathVariable Long id) {
        return episodeDtoMapper.toDto(episodeService.findById(id));
    }

    @GetMapping("/all")
    public List<EpisodeResponseDto> getAll() {
        return episodeService.findAll().stream()
                .map(episodeDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
