package springboot.rickandmorty.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "movie_character")
public class MovieCharacter {
    @Id
    @GeneratedValue(generator = "movie_character_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "movie_character_id_seq",
                sequenceName = "movie_character_id_seq",
                allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    private String species;
    private String type;
    private String image;
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
    @JoinTable(name = "movie_character_episode",
            joinColumns = @JoinColumn(name = "movie_character_id"),
            inverseJoinColumns = @JoinColumn(name = "episode_id"))
    private List<Episode> episodes;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String url;
    private LocalDateTime created;
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Location location;
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Location origin;
}
