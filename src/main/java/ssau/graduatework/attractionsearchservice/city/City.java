package ssau.graduatework.attractionsearchservice.city;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_seq_generator")
    @SequenceGenerator(name = "city_id_seq_generator",
            sequenceName = "city_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Attraction> attractionList = new ArrayList<>();
}
