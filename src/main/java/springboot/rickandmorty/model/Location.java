package springboot.rickandmorty.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(generator = "location_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "location_id_seq",
            sequenceName = "location_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    private String url;
    private LocalDateTime created;
}
