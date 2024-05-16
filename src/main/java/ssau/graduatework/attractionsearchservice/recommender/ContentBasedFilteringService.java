package ssau.graduatework.attractionsearchservice.recommender;

import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.AttractionRepository;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.tag.Tag;
import ssau.graduatework.attractionsearchservice.user.UserRepository;

import java.util.*;

@Service
public class ContentBasedFilteringService {

    private Map<Long, Set<Long>> attractionTags;
    private UserRepository userRepository;
    private AttractionRepository attractionRepository;

    public ContentBasedFilteringService(AttractionRepository attractionRepository) {
        attractionTags = new HashMap<>();
        for (Attraction attraction : attractionRepository.findAll()) {
            Set<Long> tagIds = new HashSet<>();
            for (Tag tag : attraction.getTagList()) {
                tagIds.add(tag.getId());
            }
            attractionTags.put(attraction.getId(), tagIds);
        }
    }

    public List<Attraction> getRecommendations(Long userId) {
        // Получение оценок текущего пользователя
        Map<Long, Integer> userRatings = new HashMap<>();
        for (Review review : userRepository.findById(userId).orElse(null).getReviewList()) {
            userRatings.put(review.getAttraction().getId(), review.getRate());
        }

        // Вычисление сходства между достопримечательностями
        Map<String, Double> attractionSimilarities = new HashMap<>();
        for (Long attractionId1 : attractionTags.keySet()) {
            for (Long attractionId2 : attractionTags.keySet()) {
                if (attractionId1.equals(attractionId2)) {
                    continue;
                }
                attractionSimilarities.put(attractionId1 + "," + attractionId2, calculateSimilarity(attractionTags.get(attractionId1), attractionTags.get(attractionId2)));
            }
        }

        // Сортировка достопримечательностей по сходству
        List<Map.Entry<String, Double>> sortedSimilarities = new ArrayList<>(attractionSimilarities.entrySet());
        sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Получение рекомендаций на основе оценок похожих достопримечательностей
        Set<Attraction> recommendedAttractions = new HashSet<>();
        for (Map.Entry<String, Double> similarity : sortedSimilarities) {
            String[] attractionIds = similarity.getKey().split(",");
            Long attractionId1 = Long.parseLong(attractionIds[0]);
            Long attractionId2 = Long.parseLong(attractionIds[1]);
            if (userRatings.containsKey(attractionId1) && !userRatings.containsKey(attractionId2)) {
                recommendedAttractions.add(attractionRepository.findById(attractionId2).orElse(null));
            } else if (userRatings.containsKey(attractionId2) && !userRatings.containsKey(attractionId1)) {
                recommendedAttractions.add(attractionRepository.findById(attractionId1).orElse(null));
            }
        }

        // Возврат отсортированного списка рекомендуемых достопримечательностей
        return new ArrayList<>(recommendedAttractions);
    }

    private double calculateSimilarity(Set<Long> tags1, Set<Long> tags2) {
        // Вычисление пересечения тегов
        Set<Long> intersection = new HashSet<>(tags1);
        intersection.retainAll(tags2);

        if (intersection.isEmpty()) {
            return 0.0;
        }

        // Вычисление размера объединения тегов
        Set<Long> union = new HashSet<>(tags1);
        union.addAll(tags2);

        // Вычисление коэффициента Жаккара
        return (double) intersection.size() / union.size();
    }
}

