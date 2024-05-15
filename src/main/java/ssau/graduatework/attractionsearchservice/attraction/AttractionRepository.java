package ssau.graduatework.attractionsearchservice.attraction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {
    @Query(value = "select information, rate from public.attraction where name = ?1", nativeQuery = true)
    List<Object> getInformationAndRate(String attraction);
    Optional<Attraction> findAttractionById(Integer id);
}
