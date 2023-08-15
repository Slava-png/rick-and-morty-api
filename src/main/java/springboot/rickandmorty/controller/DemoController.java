package springboot.rickandmorty.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.rickandmorty.service.EpisodeService;
import springboot.rickandmorty.service.LocationService;
import springboot.rickandmorty.service.MovieCharacterService;

@RestController
@AllArgsConstructor
@RequestMapping("/demo")
public class DemoController {
    private final MovieCharacterService movieCharacterService;
    private final LocationService locationService;
    private final EpisodeService episodeService;

    @GetMapping
    public String get() {
        episodeService.syncExternalEpisodes();
        locationService.syncExternalLocations();
        movieCharacterService.syncExternalCharacters();
        return "Done!!";
    }
}
