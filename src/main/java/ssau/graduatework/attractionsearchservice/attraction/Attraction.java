package ssau.graduatework.attractionsearchservice.attraction;

import jakarta.persistence.*;
import lombok.*;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionCategory;
import ssau.graduatework.attractionsearchservice.city.City;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.tag.Tag;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "attraction")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attraction_id_seq_generator")
    @SequenceGenerator(name = "attraction_id_seq_generator",
            sequenceName = "attraction_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String category;

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

    public Attraction() {
        setMiddleRate();
    }

    public Double getRate() {
        setMiddleRate();
        return rate;
    }

    public void setMiddleRate() {
        if (reviewList.isEmpty()) {
            this.rate = 0.0;
        } else {
            int rates = 0;
            for (Review review : reviewList) {
                rates += review.getRate();
            }
            this.rate = (double) rates / (double) reviewList.size();
        }
    }
}
