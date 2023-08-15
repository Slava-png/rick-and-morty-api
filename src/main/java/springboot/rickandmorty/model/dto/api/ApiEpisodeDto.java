package springboot.rickandmorty.model.dto.api;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiEpisodeDto {
    private Long id;
    private String name;
    private String air_date;
    private String episode;
    private String url;
    private LocalDateTime created;
}
