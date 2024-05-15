package ssau.graduatework.attractionsearchservice.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.user.User;

@Entity
@Getter
@Setter
@Table(name = "review")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_seq_generator")
    @SequenceGenerator(name = "review_id_seq_generator",
            sequenceName = "review_id_seq", allocationSize = 1)
    private Integer id;

    private Integer rate;

    private String text;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public Review(Integer rate, String text) {
        this.rate = rate;
        this.text = text;
    }
}
