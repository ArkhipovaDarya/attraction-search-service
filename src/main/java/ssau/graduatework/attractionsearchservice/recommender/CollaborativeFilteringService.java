package ssau.graduatework.attractionsearchservice.recommender;

import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.AttractionRepository;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.user.User;
import ssau.graduatework.attractionsearchservice.user.UserRepository;

import java.util.*;

@Service
public class CollaborativeFilteringService {

    // Матрица оценок пользователей к достопримечательностям
    private Map<Long, Map<Long, Double>> userAttractionRatings;
    private AttractionRepository attractionRepository;

    // Конструктор для инициализации матрицы оценок
    public CollaborativeFilteringService(UserRepository userRepository, AttractionRepository attractionRepository) {
        // Получение всех пользователей и достопримечательностей из базы данных
        List<User> users = userRepository.findAll();
        List<Attraction> attractions = attractionRepository.findAll();

        // Создание матрицы оценок
        userAttractionRatings = new HashMap<>();
        for (User user : users) {
            userAttractionRatings.put(user.getId(), new HashMap<>());
            for (Attraction attraction : attractions) {
                // Поиск отзыва пользователя для данной достопримечательности
                Review review = attraction.getReviewList().stream()
                        .filter(r -> r.getUser().getId().equals(user.getId()))
                        .findFirst()
                        .orElse(null);

                // Получение оценки или установка ее на 0, если отзыв не найден
                Double rating = review != null ? review.getRate() : 0.0;
                userAttractionRatings.get(user.getId()).put(attraction.getId(), rating);
            }
        }
    }

    // Метод для получения рекомендуемых достопримечательностей для пользователя
    public List<Attraction> getRecommendations(Long userId) {
        // Получение оценок текущего пользователя
        Map<Long, Double> userRatings = userAttractionRatings.get(userId);

        // Вычисление сходства между текущим пользователем и всеми остальными пользователями
        Map<Long, Double> userSimilarities = new HashMap<>();
        for (Long otherUserId : userAttractionRatings.keySet()) {
            if (otherUserId.equals(userId)) {
                continue;
            }
            userSimilarities.put(otherUserId, calculateSimilarity(userRatings, userAttractionRatings.get(otherUserId)));
        }

        // Сортировка пользователей по сходству
        List<Map.Entry<Long, Double>> sortedSimilarities = new ArrayList<>(userSimilarities.entrySet());
        sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Получение рекомендаций на основе оценок похожих пользователей
        Set<Attraction> recommendedAttractions = new HashSet<>();
        for (Map.Entry<Long, Double> similarity : sortedSimilarities) {
            Map<Long, Double> otherUserRatings = userAttractionRatings.get(similarity.getKey());
            for (Long attractionId : otherUserRatings.keySet()) {
                if (!userRatings.containsKey(attractionId)) {
                    recommendedAttractions.add(attractionRepository.findById(attractionId).orElse(null));
                }
            }
        }

        // Возврат отсортированного списка рекомендуемых достопримечательностей
        return new ArrayList<>(recommendedAttractions);
    }

    // Метод для вычисления сходства между двумя векторами оценок
    private double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        // Вычисление пересечения оценок
        Set<Long> intersection = new HashSet<>(vector1.keySet());
        intersection.retainAll(vector2.keySet());

        if (intersection.isEmpty()) {
            return 0.0;
        }

        // Вычисление среднего оценок для пересечения
        double mean1 = vector1.values().stream().reduce(0.0, Double::sum) / intersection.size();
        double mean2 = vector2.values().stream().reduce(0.0, Double::sum) / intersection.size();

        // Вычисление косинусного сходства
        double numerator = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;
        for (Long attractionId : intersection) {
            double rating1 = vector1.get(attractionId) - mean1;
            double rating2 = vector2.get(attractionId) - mean2;
            numerator += rating1 * rating2;
            denominator1 += rating1 * rating1;
            denominator2 += rating2 * rating2;
        }

        return numerator / (Math.sqrt(denominator1) * Math.sqrt(denominator2));
    }
}