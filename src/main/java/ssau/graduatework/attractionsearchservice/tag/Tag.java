package ssau.graduatework.attractionsearchservice.tag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq_generator")
    @SequenceGenerator(name = "tag_id_seq_generator",
            sequenceName = "tag_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "attractions_tags",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "attraction_id")})
    public List<Attraction> attractionList = new ArrayList<>();
}
