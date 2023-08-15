package springboot.rickandmorty.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "episode")
public class Episode {
    @Id
    @GeneratedValue(generator = "episode_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "episode_id_seq",
            sequenceName = "episode_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    @Column(name = "air_date")
    private String airDate;
    private String episode;
    private String url;
    private LocalDateTime created;

    public Episode(Long externalId) {
        this.externalId = externalId;
    }
}
