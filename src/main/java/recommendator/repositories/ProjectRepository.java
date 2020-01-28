package recommendator.repositories;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import recommendator.exceptions.NotFoundException;
import recommendator.models.entities.Client;
import recommendator.models.entities.Project;

import java.util.List;

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

    @Transactional
    public List<Project> listByClient(Client client) {
        return em.createQuery("SELECT p FROM Project p WHERE p.client = :client", Project.class)
                .setParameter("client", client)
                .getResultList();
    }

    @Transactional
    public Project getByApiKeyAndClient(String key, Client client) {
        return em.createQuery("SELECT p FROM Project p WHERE p.apiKey = :key AND p.client = :client", Project.class)
                .setParameter("key", key)
                .setParameter("client", client)
                .getResultList().stream().findFirst().orElseThrow(() -> new NotFoundException("Project not found"));
    }
}
