package springboot.rickandmorty.model.dto.api;

import lombok.Data;
import java.util.List;

@Data
public class ApiCharacterResponseDto {
    private ApiInfoDto info;
    private List<ApiCharacterDto> results;
}
