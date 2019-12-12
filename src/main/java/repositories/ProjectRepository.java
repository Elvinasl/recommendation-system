package repositories;


import models.Project;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
public class ProjectRepository extends DatabaseRepository<Project> {

    ProjectRepository() {
        super(Project.class);
    }


    @Transactional
    public Project getByApiKey(String key) {
        TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p WHERE p.apiKey = ?1", Project.class);
        query.setParameter(1, key);
        return query.getSingleResult();
    }
}
