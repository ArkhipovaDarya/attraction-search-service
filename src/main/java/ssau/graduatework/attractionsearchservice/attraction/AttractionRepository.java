package ssau.graduatework.attractionsearchservice.attraction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    Attraction findAttractionById(Long id);
}
