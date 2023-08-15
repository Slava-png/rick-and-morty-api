package springboot.rickandmorty.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieCharacterResponseDto {
    private Long id;
    private String name;
    private String species;
    private String type;
    private String image;
    private List<EpisodeResponseDto> episodes;
    private String gender;
    private String status;
    private String url;
    private LocalDateTime created;
    private LocationResponseDto location;
    private LocationResponseDto origin;
}
