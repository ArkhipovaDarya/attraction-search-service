package ssau.graduatework.attractionsearchservice.recommender;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.relational.core.sql.In;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.review.ReviewRepository;
import ssau.graduatework.attractionsearchservice.user.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.NaN;
import static java.lang.Math.*;

@Getter
public class Cluster {

    private User centroid;
    private List<User> users;
    private boolean changed;

    private ReviewRepository reviewRepository;

    public Cluster(User centroid) {
        this.centroid = centroid;
        this.users = new ArrayList<>();
        this.changed = false;
    }

    public void addUser(User attraction) {
        users.add(attraction);
        changed = true;
    }

    public void updateCentroid() {
        if (!this.users.isEmpty()) {
            List<Double> distances = new ArrayList<>();
            for (User user: users) {
                distances.add(getDistance(user, this.centroid));
            }
            double averageDist = distances.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .getAsDouble();
            int ind = -1;
            double d = 1.0;
            for (User u : users) {
                if (abs(getDistance(u, this.centroid) - averageDist) < d) {
                    d = abs(getDistance(u, this.centroid) - averageDist);
                    ind = users.indexOf(u);
                }
            }
            if (ind != -1) {
                User newCentroid = users.get(ind);
                this.users.add(this.centroid);
                this.users.remove(users.get(ind));
                this.centroid = new User(newCentroid);
                changed = true;
            } else {
                changed = false;
            }
        }
        changed = false;
    }

    public double getDistance(User user, User centroid) {
        List<Long> user1Ratings = user.getReviewList().stream().map(review -> review.getAttraction().getId()).toList();
        List<Long> user2Ratings = centroid.getReviewList().stream().map(review -> review.getAttraction().getId()).toList();

        int intersectionSize = 0;

        for (Long id : user1Ratings) {
            if (user2Ratings.contains(id)) {
                intersectionSize++;
            }
        }
        int unionSize = user1Ratings.size() + user2Ratings.size() - intersectionSize;

        return 1.0 - 1.0 * intersectionSize / unionSize;
    }

}
