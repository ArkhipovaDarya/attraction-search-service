package ssau.graduatework.attractionsearchservice.attraction;

import ssau.graduatework.attractionsearchservice.attraction.dto.AttractionDto;
import ssau.graduatework.attractionsearchservice.review.dto.ReviewDto;

import java.util.List;

public interface AttractionService {
    List<Attraction> getAttractionsByLongitudeAndLatitude(Double longitude, Double latitude);
    List<Attraction> getAttractionsByLongitudeAndLatitudeAndCategory(Double longitude, Double latitude, String category);
    List<Attraction> getAttractionsByLongitudeAndLatitudeAndRate(Double longitude, Double latitude, Double midRate);
    List<Attraction> getAttractionsByCityAndCategory(String city, String category);
    List<Attraction> getAttractionsByCityAndRate(String city, Double midRate);
    List<Attraction> getAttractionByCity(String city);
    List<Object> getInformationAndRate(String attraction);
    List<Attraction> showAllAttractions();
    List<AttractionDto> createAttractionList(List<Attraction> attractions);
    List<ReviewDto> showReviewList(String attraction);
    void setReview(String attraction, Integer rate, String review);
    Attraction addNewAttraction(Attraction attraction);
    AttractionDto updateAttraction(AttractionDto attractionDto);
}
