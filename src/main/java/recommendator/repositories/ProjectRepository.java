package recommendator.repositories;


import recommendator.exceptions.NotFoundException;
import recommendator.models.entities.Project;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class ProjectRepository extends DatabaseRepository<Project> {

    ProjectRepository() {
        super(Project.class);
    }

    @Transactional
    public Project getByApiKey(String key) {
        return em.createQuery("SELECT p FROM Project p WHERE p.apiKey = ?1", Project.class)
              .setParameter(1, key)
                .getResultList().stream().findFirst().orElseThrow(() -> new NotFoundException("Project not found"));
    }
}
