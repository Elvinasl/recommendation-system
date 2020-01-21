package recommendator.repositories;

import recommendator.models.entities.Cell;
import recommendator.models.entities.UserPreference;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserPreferenceRepository extends DatabaseRepository<UserPreference> {

    public UserPreferenceRepository() {
        super(UserPreference.class);
    }

    @Transactional
    public UserPreference getByCellOrNull(Cell cell) {
        Query query = em.createQuery("SELECT up FROM UserPreference up WHERE up.cell = :cell")
                .setParameter("cell", cell);
        try {
            return (UserPreference) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
