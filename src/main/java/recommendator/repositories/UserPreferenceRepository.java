package recommendator.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import recommendator.models.entities.Cell;
import recommendator.models.entities.UserPreference;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserPreferenceRepository extends DatabaseRepository<UserPreference> {

    public UserPreferenceRepository() {
        super(UserPreference.class);
    }

    /**
     * Gathers {@link UserPreference} from the database for a specific {@link Cell}
     *
     * @param cell the {@link UserPreference} should belong to
     * @return UserPreference from the given Cell or null if nothing was found
     */
    @Transactional
    public UserPreference getByCellOrNull(Cell cell) {
        Query query = em.createQuery("SELECT up FROM UserPreference up WHERE up.cell = :cell")
                .setParameter("cell", cell);
        try {
            return (UserPreference) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
