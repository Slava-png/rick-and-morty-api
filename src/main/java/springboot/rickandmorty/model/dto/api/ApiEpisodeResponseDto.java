package springboot.rickandmorty.model.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class ApiEpisodeResponseDto {
    private ApiInfoDto info;
    private List<ApiEpisodeDto> results;
}
