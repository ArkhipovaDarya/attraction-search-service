package ssau.graduatework.attractionsearchservice.recommender;

import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.AttractionRepository;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.user.User;
import ssau.graduatework.attractionsearchservice.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborativeFilteringService {
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;

    public CollaborativeFilteringService(UserRepository userRepository, AttractionRepository attractionRepository) {
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
    }

    public Map<Attraction, Double> getRecommendations(Long userId) {
        // Get the user with the given id
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get all attractions that the user has reviewed
        List<Review> userReviews = user.getReviewList();
        List<Attraction> attractionIds = userReviews.stream().map(Review::getAttraction).toList();

        // Get all attractions that other users have reviewed
        List<Attraction> allAttractions = attractionRepository.findAll();
        List<Attraction> otherUserAttractions = allAttractions.stream()
                .filter(attraction -> !attractionIds.contains(attraction))
                .toList();

        // Create a user-attraction matrix
        Map<Long, Map<Long, Double>> userAttractionMatrix = new HashMap<>();
        for (User otherUser : userRepository.findAll()) {
            userAttractionMatrix.put(otherUser.getId(), new HashMap<>());

            for (Review review : otherUser.getReviewList()) {
                userAttractionMatrix.get(otherUser.getId()).put(review.getAttraction().getId(), Double.valueOf(review.getRate()));
            }
        }

        // Apply k-means clustering to the user-attraction matrix
        KMeansClustering kMeansClustering = new KMeansClustering(2);
        Map<Integer, List<Long>> clusters = kMeansClustering.cluster(userAttractionMatrix);

        // Get the cluster that the user belongs to
        int userCluster = -1;
        for (Map.Entry<Integer, List<Long>> entry : clusters.entrySet()) {
            if (entry.getValue().contains(userId)) {
                userCluster = entry.getKey();
                break;
            }
        }

        // Get the attractions that other users in the same cluster have reviewed highly
        Map<Attraction, Double> recommendations = new HashMap<>();
        for (Long attractionId : clusters.get(userCluster)) {
            Attraction attraction = attractionRepository.findById(attractionId).orElseThrow(() -> new IllegalArgumentException("Attraction not found"));
            double averageRating = attractionRepository.findAttractionById(attractionId).get().getRate();
            recommendations.put(attraction, averageRating);
        }

        // Sort the recommendations by average rating
        Map<Attraction, Double> sortedRecommendations = recommendations.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedRecommendations;
    }
}