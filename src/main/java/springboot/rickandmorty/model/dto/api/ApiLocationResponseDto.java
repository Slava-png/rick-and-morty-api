package springboot.rickandmorty.model.dto.api;

import java.util.List;
import lombok.Data;

@Data
public class ApiLocationResponseDto {
    private ApiInfoDto info;
    private List<ApiLocationDto> results;
}
