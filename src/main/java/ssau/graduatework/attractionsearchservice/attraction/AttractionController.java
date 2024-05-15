package ssau.graduatework.attractionsearchservice.attraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssau.graduatework.attractionsearchservice.review.dto.ReviewDto;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {
    private final AttractionService attractionService;

    @Autowired
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttractionDto>> getAll() {
        List<Attraction> attractions = attractionService.showAllAttractions();
        return new ResponseEntity<>(attractionService.createAttractionList(attractions), HttpStatus.OK);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<AttractionDto>> getByCityName(@PathVariable String city) {
        List<Attraction> attractionByCity = attractionService.getAttractionByCity(city);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionByCity), HttpStatus.OK);
    }

    @GetMapping("/near/{longitude}/{latitude}")
    public ResponseEntity<List<AttractionDto>> getAttrByLoAndLa(@PathVariable Double longitude, @PathVariable Double latitude) {
        List<Attraction> attractionByLL = attractionService.getAttractionsByLongitudeAndLatitude(longitude, latitude);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionByLL), HttpStatus.OK);
    }

    @GetMapping("/near/{longitude}/{latitude}/category/{category}")
    public ResponseEntity<List<AttractionDto>> getAttrByCat(@PathVariable Double longitude, @PathVariable Double latitude, @PathVariable String category) {
        List<Attraction> attractionByLLC = attractionService.getAttractionsByLongitudeAndLatitudeAndCategory(longitude, latitude, category);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionByLLC), HttpStatus.OK);

    }

    @GetMapping("/near/{longitude}/{latitude}/rate/{midrate}")
    public ResponseEntity<List<AttractionDto>> getAttrByMidRate(@PathVariable Double longitude, @PathVariable Double latitude, @PathVariable Double midrate) {
        List<Attraction> attractionByLLMR = attractionService.getAttractionsByLongitudeAndLatitudeAndRate(longitude, latitude, midrate);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionByLLMR), HttpStatus.OK);
    }

    @GetMapping("/city/{city}/category/{category}")
    public ResponseEntity<List<AttractionDto>> getAttrByCityAndCat(@PathVariable String city, @PathVariable String category) {
        List<Attraction> attractionList = attractionService.getAttractionsByCityAndCategory(city, category);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionList), HttpStatus.OK);
    }

    @GetMapping("/city/{city}/rate/{midrate}")
    public ResponseEntity<List<AttractionDto>> getAttrByCityAndMidRate(@PathVariable String city, @PathVariable Double midrate) {
        List<Attraction> attractionList = attractionService.getAttractionsByCityAndRate(city, midrate);
        return new ResponseEntity<>(attractionService.createAttractionList(attractionList), HttpStatus.OK);
    }

    @GetMapping("/{attraction}")
    public ResponseEntity<Object> getOnlyInfoAndMidRate(@PathVariable String attraction) {
        return new ResponseEntity<>(attractionService.getInformationAndRate(attraction), HttpStatus.OK);
    }

    @GetMapping("/{attraction}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewList(@PathVariable String attraction) {
        return new ResponseEntity<>(attractionService.showReviewList(attraction), HttpStatus.OK);
    }

    @PostMapping("/{attraction}/reviews/new/{rate}/{review}")
    public ResponseEntity<List<ReviewDto>> setReviewForAttraction(@PathVariable String attraction, @PathVariable Integer rate, @PathVariable String review) {
        attractionService.setReview(attraction, rate, review);
        return new ResponseEntity<>(attractionService.showReviewList(attraction), HttpStatus.OK);
    }
}
