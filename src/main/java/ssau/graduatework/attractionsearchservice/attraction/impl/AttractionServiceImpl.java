package ssau.graduatework.attractionsearchservice.attraction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.attraction.*;
import ssau.graduatework.attractionsearchservice.attraction.AttractionDto;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionCategory;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionFields;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionMapper;
import ssau.graduatework.attractionsearchservice.review.Review;
import ssau.graduatework.attractionsearchservice.review.dto.ReviewDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {
    private final AttractionRepository attractionRepository;
    private final AttractionRepositoryOwn attractionRepositoryOwn;
    Map<AttractionFields, Object> fieldsMap;
    private final AttractionMapper attractionMapper;

    //list of nearby attractions
    @Override
    public List<Attraction> getAttractionsByLongitudeAndLatitude(Double longitude, Double latitude) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.LONGITUDE, longitude);
        fieldsMap.put(AttractionFields.LATITUDE, latitude);
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //list of nearby attractions filtered by category
    @Override
    public List<Attraction> getAttractionsByLongitudeAndLatitudeAndCategory(Double longitude, Double latitude, String category) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.LONGITUDE, longitude);
        fieldsMap.put(AttractionFields.LATITUDE, latitude);
        fieldsMap.put(AttractionFields.CATEGORY, convertCategory(category));
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //list of nearby attractions filtered by middle rate
    @Override
    public List<Attraction> getAttractionsByLongitudeAndLatitudeAndRate(Double longitude, Double latitude, Double midRate) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.LONGITUDE, longitude);
        fieldsMap.put(AttractionFields.LATITUDE, latitude);
        fieldsMap.put(AttractionFields.RATE, midRate);
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //list attractions in the selected city filtered by category and sorted by longitude
    @Override
    public List<Attraction> getAttractionsByCityAndCategory(String city, String category) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.CITY, city);
        fieldsMap.put(AttractionFields.CATEGORY, convertCategory(category));
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //list of attractions in the selected city filtered by middle rate and sorted by longitude
    @Override
    public List<Attraction> getAttractionsByCityAndRate(String city, Double midRate) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.CITY, city);
        fieldsMap.put(AttractionFields.RATE, midRate);
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //list of nearby attractions in a city
    @Override
    public List<Attraction> getAttractionByCity(String city) {
        fieldsMap = new HashMap<>();
        fieldsMap.put(AttractionFields.CITY, city);
        return attractionRepositoryOwn.getAttractionsByCriteria(fieldsMap);
    }

    //string with a description of the attraction and its middle rate
    @Override
    public List<Object> getInformationAndRate(String attraction) {
        return attractionRepository.getInformationAndRate(attraction);
    }

    @Override
    public List<Attraction> showAllAttractions() {
        return attractionRepository.findAll();
    }

    //generating clear output for attraction
    @Override
    public List<AttractionDto> createAttractionList(List<Attraction> attractions) {
        List<AttractionDto> dto = new ArrayList<>();
        for (Attraction attraction: attractions) {
            dto.add(new AttractionDto(attraction.getId(), attraction.getName(), attraction.getCategory(),
                    attraction.getLongitude(), attraction.getLatitude(), attraction.getInformation(), attraction.getRate(), attraction.getCity().getName()));
        }
        return dto;
    }

    @Override
    public List<ReviewDto> showReviewList(String attraction) {
        return null;
    }

    @Override
    public void setReview(String attraction, Integer rate, String review) {
        Attraction attr = new Attraction();
        for (Attraction a : showAllAttractions()) {
            if (a.getName().equals(attraction)) attr = a;
        }
        Review rev = new Review(rate, review);
        rev.setAttraction(attr);
        attr.getReviewList().add(rev);
        attr.setMiddleRate();
        attractionRepository.save(attr);
    }

    @Override
    public Attraction addNewAttraction(Attraction attraction) {
        attractionRepository.saveAndFlush(attraction);
        return attraction;
    }

    @Override
    public AttractionDto updateAttraction(AttractionDto attractionDto) {
        Attraction actual = attractionRepository.findAttractionById(attractionDto.getId()).orElseThrow();
        actual.setInformation(attractionDto.getInformation());
        return attractionMapper.toDto(attractionRepository.saveAndFlush(actual));
    }

    //for converting from string to enum
    AttractionCategory convertCategory(String category) {
        category = category.toLowerCase().replace(" ", "_");
        return switch (category) {
            case "military" -> AttractionCategory.MILITARY;
            case "culture" -> AttractionCategory.CULTURE;
            case "religious" -> AttractionCategory.RELIGIOUS;
            case "historical" -> AttractionCategory.HISTORICAL;
            case "nature" -> AttractionCategory.NATURE;
            default -> AttractionCategory.ANOTHER;
        };
    }
}
