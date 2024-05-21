package ssau.graduatework.attractionsearchservice.recommender;

import lombok.Getter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.relational.core.sql.In;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.user.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.NaN;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
public class Cluster {

    private User centroid;
    private List<User> users;
    private boolean changed;

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
            User userMax = this.users.stream()
                    .max(Comparator.comparingInt(user -> user.getReviewList().size())).get();
            this.users.add(this.centroid);
            this.users.remove(userMax);
            this.centroid = new User(userMax);
            this.changed = false;
        }
    }

    public double getDistance(User user, User centroid) {
        double dotProduct = 0.0;
        List<Integer> user1Ratings = user.getReviewList().stream().map(Review::getRate).toList();
        List<Integer> user2Ratings = centroid.getReviewList().stream().map(Review::getRate).toList();

        int intersectionSize = 0;
        int unionSize = user1Ratings.size() + user2Ratings.size();

        for (Integer rating : user1Ratings) {
            if (user2Ratings.contains(rating)) {
                intersectionSize++;
            }
        }

        return 1.0 * intersectionSize / unionSize;
    }

    private static double calculateMagnitude(List<Integer> ratings) {
        double magnitude = 0.0;
        for (Integer rating : ratings) {
            magnitude += Math.pow(rating, 2);
        }
        return Math.sqrt(magnitude);
    }

    public double getDistance(double avgRating, double rate) {
        double dotProduct = avgRating * rate;

        double attractionNorm = Math.sqrt(avgRating * avgRating);
        double centroidNorm = Math.sqrt(rate * rate);

        return dotProduct / (attractionNorm * centroidNorm);
    }

}
