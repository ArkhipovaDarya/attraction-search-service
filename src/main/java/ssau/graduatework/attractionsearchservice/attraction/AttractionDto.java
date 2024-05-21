package ssau.graduatework.attractionsearchservice.attraction;

import lombok.Getter;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionCategory;

@Getter
public class AttractionDto {
    private final Long id;
    private final String name;
    private final String category;
    private final Double longitude;
    private final Double latitude;
    private final String information;
    private final Double rate;
    private final String city;

    public AttractionDto(Long id, String name, String category, Double longitude, Double latitude, String information, Double rate, String city) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
        this.information = information;
        this.rate = rate;
        this.city = city;
    }
}