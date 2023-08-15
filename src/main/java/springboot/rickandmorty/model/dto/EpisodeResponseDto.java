package springboot.rickandmorty.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodeResponseDto {
    private Long id;
    private String name;
    private String airDate;
    private String episode;
    private String url;
    private LocalDateTime created;
}
