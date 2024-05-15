package ssau.graduatework.attractionsearchservice.attraction.util;

import org.springframework.stereotype.Component;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.AttractionRepository;
import ssau.graduatework.attractionsearchservice.attraction.dto.AttractionDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttractionMapper {
    private AttractionRepository attractionRepository;

    public AttractionDto toDto(Attraction attraction){
        return new AttractionDto(
                attraction.getId(),
                attraction.getName(),
                attraction.getCategory(),
                attraction.getLongitude(),
                attraction.getLatitude(),
                attraction.getInformation(),
                attraction.getRate(),
                attraction.getCity().getName()
        );
    }

    public List<AttractionDto> mapToDtoList(List<Attraction> attractionList){
        return attractionList.stream().map(this::toDto).collect(Collectors.toList());
    }

    /*public Attraction toAttraction(AttractionDto attractionDto){
        City city = new City();
        city.setName(attractionDto.getCity());
        return new Attraction(
                attractionDto.getName(),
                attractionDto.getCategory(),
                attractionDto.getLongitude(),
                attractionDto.getLatitude(),
                attractionDto.getInformation(),
                city
        );
    }*/
}
