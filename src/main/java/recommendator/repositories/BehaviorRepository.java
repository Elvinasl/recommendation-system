package recommendator.repositories;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;

import java.util.List;

@Repository
public class BehaviorRepository extends DatabaseRepository<Behavior> {

    BehaviorRepository() {
        super(Behavior.class);
    }

    /**
     * Gathers all the {@link Behavior} records from the database for a specific user and project.
     * It also filters the records by like or dislike.
     * @param user the {@link Behavior} belongs to
     * @param liked or disliked
     * @param project the {@link recommendator.models.entities.Row} belongs to
     * @return list of all the {@link Behavior}'s
     */
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

    /**
     * Gathers all the {@link Behavior} records from the database for a specific user
     * @param user the {@link Behavior} belongs to
     * @return list of all the {@link Behavior}'s
     */
    @Transactional
    public List<Behavior> getBehaviorsByUser(User user) {
        return em.createQuery("SELECT b " +
                "FROM Behavior b " +
                "JOIN FETCH b.row r " +
                "JOIN FETCH r.cells " +
                "WHERE b.user = :user ", Behavior.class)
                .setParameter("user", user)
                .getResultList();
    }
}
