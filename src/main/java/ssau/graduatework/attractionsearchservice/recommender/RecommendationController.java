package ssau.graduatework.attractionsearchservice.recommender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.AttractionDto;
import ssau.graduatework.attractionsearchservice.attraction.AttractionRepository;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionMapper;
import ssau.graduatework.attractionsearchservice.user.UserRepository;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private CollaborativeFilteringService collaborativeFilteringService;
    @Autowired
    private AttractionMapper attractionMapper;

    @GetMapping("/{userId}")
    public List<AttractionDto> getRecommendations(@PathVariable Long userId) throws IOException {

        // Получение рекомендаций с использованием коллаборативной фильтрации
        List<Attraction> collaborativeRecommendations = collaborativeFilteringService.getRecommendations(userId);

        // Получение рекомендаций с использованием контентной фильтрации
        //List<Attraction> contentBasedRecommendations = contentBasedFilteringService.getRecommendations(userId);

        // Объединение рекомендаций из обоих методов и присвоение им равного веса
        //Set<AttractionDto> combinedRecommendations = new HashSet<>();
        //combinedRecommendations.addAll(collaborativeRecommendations);
        //combinedRecommendations.addAll(contentBasedRecommendations);
        // Возврат отсортированного списка объединенных рекомендаций
        //return new ArrayList<>(combinedRecommendations);
        return attractionMapper.mapToDtoList(collaborativeRecommendations);
    }
}
