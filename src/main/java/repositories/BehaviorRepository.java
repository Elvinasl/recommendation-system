package repositories;


import models.Behavior;
import models.Project;
import models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BehaviorRepository extends DatabaseRepository<Behavior> {

    BehaviorRepository() {
        super(Behavior.class);
    }

    @Transactional
    public List<Behavior> getBehaviorsByUserAndTypeAndProject(User user, boolean liked, Project project) {
        return em.createQuery("SELECT b " +
                "FROM Behavior b " +
                "INNER JOIN b.user u " +
                "WHERE b.liked = :liked " +
                "AND u.project = :project " +
                "AND b.user = :user ", Behavior.class)
                .setParameter("user", user)
                .setParameter("liked", liked)
                .setParameter("project", project)
                .getResultList();
    }
}
