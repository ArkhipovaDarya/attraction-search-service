package ssau.graduatework.attractionsearchservice.attraction.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ssau.graduatework.attractionsearchservice.attraction.Attraction;
import ssau.graduatework.attractionsearchservice.attraction.util.AttractionFields;
import ssau.graduatework.attractionsearchservice.attraction.OwnAttractionRepository;

import java.util.List;
import java.util.Map;

@Repository
public class OwnAttractionRepositoryImpl implements OwnAttractionRepository {
    private final EntityManager entityManager;

    public OwnAttractionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Attraction> getAttractionsByCriteria(Map<AttractionFields, Object> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Attraction> criteriaQuery = criteriaBuilder.createQuery(Attraction.class);
        Root<Attraction> root = criteriaQuery.from(Attraction.class);
        Predicate[] predicates = new Predicate[criteria.size()];
        int i = 0;
        for (Map.Entry<AttractionFields, Object> af : criteria.entrySet()) {
            switch (af.getKey()) {
                case LONGITUDE -> predicates[i] = criteriaBuilder.between(root.get("longitude"), Math.abs((Double) af.getValue() - 15), ((Double) af.getValue() + 15));
                case LATITUDE -> predicates[i] = criteriaBuilder.between(root.get("latitude"), Math.abs((Double) af.getValue() - 15), ((Double) af.getValue() + 15));
                case CATEGORY -> predicates[i] = criteriaBuilder.equal(root.get("category"), af.getValue());
                case RATE -> predicates[i] = criteriaBuilder.gt(root.get("rate"), ((Double) af.getValue() - 0.1));
                case CITY -> predicates[i] = root.get("city").get("name").in(af.getValue());
            }
            i++;
        }
        criteriaQuery.select(root).where(predicates).orderBy(criteriaBuilder.asc(root.get("longitude")), criteriaBuilder.asc(root.get("latitude")));
        TypedQuery<Attraction> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
