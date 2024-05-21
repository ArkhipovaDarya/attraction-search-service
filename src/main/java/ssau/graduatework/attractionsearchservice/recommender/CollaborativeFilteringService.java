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

    public List<Attraction> getRecommendations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // получаем все отзывы пользователя
        List<Review> userReviews = user.getReviewList();
        List<Long> userAttractionIds = userReviews.stream()
                .map(review -> review.getAttraction().getId()).toList();
        List<Attraction> allAttractions = attractionRepository.findAll();
        // получаем все непосещенные пользователем достопримечательности
        List<Attraction> unvisitedAttractions = allAttractions.stream()
                .filter(attraction -> !userAttractionIds.contains(attraction.getId())).toList();

        List<Attraction> userAttractions = allAttractions.stream().filter(attraction -> userAttractionIds.contains(attraction.getId())).toList();
        List<List<Review>> otherUsersReviews = userAttractions.stream().map(Attraction::getReviewList).toList();
        List<List<User>> users = otherUsersReviews.stream().map(reviews -> reviews.stream().map(Review::getUser).toList()).toList();

        List<User> userList = userRepository.findAll();

        List<Cluster> clusters = kMeansClustering(userList, 3);
        Cluster largestCluster = clusters.stream().max(Comparator.comparing(cluster -> cluster.getUsers().size())).get();
        clusters.remove(largestCluster);

        for (Cluster otherCluster : clusters) {
            largestCluster.getUsers().removeIf(us -> otherCluster.getUsers().contains(us) || Objects.equals(otherCluster.getCentroid().getId(), us.getId()));
        }
        clusters.add(largestCluster);

        // Найти наиболее похожий кластер к средним оценкам пользователя
        Cluster closestCluster = getClosestClusterUser(clusters, user);

        System.out.println("Наиболее подходящий кластер: " + clusters.indexOf(closestCluster));

        for (Cluster cluster: clusters) {
            cluster.getUsers().removeIf(user1 -> user1.getId().equals(userId));
        }

        for (Cluster cluster : clusters) {
            System.out.print("Пользователи кластера " + clusters.indexOf(cluster) + ": ");
            Set<Long> aids = cluster.getUsers().stream().map(User::getId).collect(Collectors.toSet());
            if (!cluster.getCentroid().getId().equals(userId)) {
                System.out.print(cluster.getCentroid().getId());
            }
            for (Long ids : aids) {
                System.out.print(" " + ids);
            }
            System.out.println("");
        }


        List<List<Review>> reviews = closestCluster.getUsers().stream().map(User::getReviewList).toList();
        List<List<Attraction>> attractions = reviews.stream().map(reviews1 -> reviews1.stream().map(Review::getAttraction).toList()).toList();

        Set<Attraction> uniqueAttractions = attractions.stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        List<Attraction> recommendedAttractions = unvisitedAttractions.stream()
                .filter(uniqueAttractions::contains)
                .sorted(Comparator.comparing(Attraction::getRate).reversed()).toList();

        // Вернуть список достопримечательностей, принадлежащих наиболее похожему кластеру
        return recommendedAttractions;
    }

    private List<Cluster> kMeansClustering(List<User> users, int numClusters) {
        // Инициализировать центроиды
        List<Cluster> centroids = initCentroids(users, numClusters);

        // Итерировать, пока не будут удовлетворены условия остановки
        do {
            // Назначить каждую достопримечательность ближайшему центроиду
            for (User user : users) {
                centroids = getClosestCluster(centroids, user);
            }

            // Пересчитать центроиды
            for (Cluster cluster : centroids) {
                cluster.updateCentroid();
            }

            // Проверить условия остановки
        } while (!isStable(centroids));

        return centroids;
    }
    private List<Cluster> initCentroids(List<User> users, int numClusters) {
        // Выбрать случайным образом numClusters достопримечательностей из списка
        List<Cluster> centroids = new ArrayList<>();
        Random random = new Random();
        Set<Integer> usedIndices = new HashSet<>();

        for (int i = 0; i < numClusters; i++) {
            int index = random.nextInt(users.size());
            while (usedIndices.contains(index)) {
                index = random.nextInt(users.size());
            }
            usedIndices.add(index);
            centroids.add(new Cluster(users.get(index)));
        }

        return centroids;
    }


    private List<Cluster> getClosestCluster(List<Cluster> centroids, User user) {
        List<Double> distances = new ArrayList<>();
        for (Cluster cluster : centroids) {
            distances.add(cluster.getDistance(user, cluster.getCentroid()));
            System.out.print("Расстояние между пользователем " + user.getId() + " и кластером " + centroids.indexOf(cluster) + ": ");
            System.out.println(distances.get(centroids.indexOf(cluster)).doubleValue());
        }
        centroids.get(argmin(distances)).addUser(user);
        return centroids;
    }

    private Cluster getClosestClusterUser(List<Cluster> clusters, User user) {
        return clusters.stream()
                .filter(c -> c.getUsers().contains(user))
                .findFirst().get();
    }

    /*private Cluster getClosestCluster(List<Cluster> clusters, List<Review> reviews) {
        // Вычислить среднюю оценку пользователя по всем отзывам
        double avgRating = reviews.stream().mapToInt(Review::getRate).average().orElse(0.0);

        // Вычислить расстояние от средней оценки пользователя до каждого центроида
        List<Double> distances = new ArrayList<>();
        for (Cluster cluster : clusters) {
            distances.add(cluster.getDistance(avgRating, centroidRating));
            System.out.println("Расстояние между оценкой пользователя и центроидом " + clusters.indexOf(cluster) + " = " + distances.get(clusters.indexOf(cluster)));
        }


        // Вернуть кластер с минимальным расстоянием
        return clusters.get(argmin(distances));
    }*/

    private boolean isStable(List<Cluster> centroids) {
        // Проверить, изменился ли какой-либо центроид после последней итерации
        for (Cluster centroid : centroids) {
            if (centroid.isChanged()) {
                return false;
            }
        }

        return true;
    }

    private static int argmin(List<Double> values) {
        double min = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) < min) {
                min = values.get(i);
                index = i;
            }
        }
        return index;
    }
}

