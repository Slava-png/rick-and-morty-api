package springboot.rickandmorty.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springboot.rickandmorty.model.dto.MovieCharacterResponseDto;
import springboot.rickandmorty.service.MovieCharacterService;
import springboot.rickandmorty.service.dto.mapper.MovieCharacterDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/characters")
public class MovieCharacterController {
    private final MovieCharacterService movieCharacterService;
    private final MovieCharacterDtoMapper movieCharacterDtoMapper;

    @GetMapping("/random")
    public MovieCharacterResponseDto getRandomly() {
        return movieCharacterDtoMapper.toDto(movieCharacterService.getRandomly());
    }

    @GetMapping("/all/by-pattern")
    public List<MovieCharacterResponseDto> getByPattern(@RequestParam String pattern) {
        return movieCharacterService.findByNameInPattern(pattern)
                                    .stream()
                                    .map(movieCharacterDtoMapper::toDto)
                                    .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieCharacterResponseDto get(@PathVariable Long id) {
        return movieCharacterDtoMapper.toDto(movieCharacterService.findById(id));
    }

    @GetMapping("/all")
    public List<MovieCharacterResponseDto> getAll() {
        return movieCharacterService.findAll().stream()
                .map(movieCharacterDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
