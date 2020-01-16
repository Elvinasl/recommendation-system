package repositories;


import models.Project;
import models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserRepository extends DatabaseRepository<User> {

    UserRepository() {
        super(User.class);
    }

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
