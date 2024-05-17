package ssau.graduatework.attractionsearchservice.recommender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private CollaborativeFilteringService collaborativeFilteringService;

    @Autowired
    private ContentBasedFilteringService contentBasedFilteringService;

    @GetMapping("/{userId}")
    public List<Attraction> getRecommendations(@PathVariable Long userId) {
        // Получение рекомендаций с использованием коллаборативной фильтрации
        Map<Attraction, Double> collaborativeRecommendations = collaborativeFilteringService.getRecommendations(userId);

        // Получение рекомендаций с использованием контентной фильтрации
        List<Attraction> contentBasedRecommendations = contentBasedFilteringService.getRecommendations(userId);

        // Объединение рекомендаций из обоих методов и присвоение им равного веса
        Set<Attraction> combinedRecommendations = new HashSet<>();
        combinedRecommendations.addAll((Collection<? extends Attraction>) collaborativeRecommendations);
        combinedRecommendations.addAll(contentBasedRecommendations);

        // Возврат отсортированного списка объединенных рекомендаций
        return new ArrayList<>(combinedRecommendations);
    }
}
