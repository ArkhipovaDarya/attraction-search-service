package ssau.graduatework.attractionsearchservice.attraction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssau.graduatework.attractionsearchservice.city.City;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.tag.Tag;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attraction")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attraction_id_seq_generator")
    @SequenceGenerator(name = "attraction_id_seq_generator",
            sequenceName = "attraction_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    private AttractionCategory category;

    private Double longitude;

    private Double latitude;

    private String information;

    private Double rate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "attractions_tags",
            joinColumns = {@JoinColumn(name = "attraction_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tagList = new ArrayList<>();

    /*public void setMiddleRate() {
        if (reviewList.isEmpty()) {
            this.middleRate = 0.0;
        } else {
            int rates = 0;
            for (Review review : reviewList) {
                rates += review.getRate();
            }
            this.middleRate = (double) rates / (double) reviewList.size();
        }
    }*/
}
