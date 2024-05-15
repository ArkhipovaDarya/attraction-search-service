package ssau.graduatework.attractionsearchservice.attraction;

import ssau.graduatework.attractionsearchservice.attraction.util.AttractionFields;

import java.util.List;
import java.util.Map;

public interface AttractionRepositoryOwn {
    List<Attraction> getAttractionsByCriteria(Map<AttractionFields, Object> criteria);
}
