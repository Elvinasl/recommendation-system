package recommendator.repositories;


import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

@Repository
public class UserRepository extends DatabaseRepository<User> {

    UserRepository() {
        super(User.class);
    }

    /**
     * Gathers an {@link User} from the database that match the given userId and {@link Project}.
     * @param externalUserId, the userId given by the client
     * @param project the user belongs to
     * @return A user if any was found, otherwise null will be returned.
     */
    @Transactional
    public User findByExternalIdAndProjectOrNull(String externalUserId, Project project) {
        return getSingleResultOrNull(findByExternalIdAndProjectQuery(externalUserId, project));
    }

    private Query findByExternalIdAndProjectQuery(String externalUserId, Project project) {
        return em.createQuery("SELECT u " +
                "FROM User u " +
                "WHERE u.externalUserId = :externalUserId " +
                "AND u.project = :project ", User.class)
                .setParameter("externalUserId", externalUserId)
                .setParameter("project", project);
    }
}
