package springboot.rickandmorty.model.dto.api;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ApiCharacterDto {
    private Long id;
    private String name;
    private String species;
    private String type;
    private String image;
    private List<String> episode;
    private ApiCharacterLocationDto origin;
    private ApiCharacterLocationDto location;
    private String status;
    private String gender;
    private String url;
    private LocalDateTime created;
}
