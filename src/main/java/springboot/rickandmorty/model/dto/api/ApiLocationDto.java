package springboot.rickandmorty.model.dto.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiLocationDto {
    private Long id;
    private String name;
    private String url;
    private LocalDateTime created;
}
