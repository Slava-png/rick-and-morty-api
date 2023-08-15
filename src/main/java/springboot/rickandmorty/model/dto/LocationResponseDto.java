package springboot.rickandmorty.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationResponseDto {
    private Long id;
    private String name;
    private String url;
    private LocalDateTime created;
}
