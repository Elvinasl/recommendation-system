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

    /**
     * Gathers a {@link Project} from the database based on the given api-key.
     * @param key of the {@link Project}
     * @return a project matching the api-key
     */
    @Transactional
    public Project getByApiKey(String key) {
        return em.createQuery("SELECT p FROM Project p WHERE p.apiKey = ?1", Project.class)
              .setParameter(1, key)
                .getResultList().stream().findFirst().orElseThrow(() -> new NotFoundException("Project not found"));
    }

    /**
     * Gathers a {@link List<Project>} from the database that belong to a specific {@link Client}.
     * @param client the projects should belong to
     * @return all the projects from the given {@link Client}
     */
    @Transactional
    public List<Project> listByClient(Client client) {
        return em.createQuery("SELECT p FROM Project p WHERE p.client = :client", Project.class)
                .setParameter("client", client)
                .getResultList();
    }

    /**
     * Gathers a {@link Project} from the database that match the api-key and belongs to a specific {@link Client}.
     * @param key of the {@link Project}
     * @param client owner of the {@link Project}
     * @return project matching the key and {@link Client}
     */
    @Transactional
    public Project getByApiKeyAndClient(String key, Client client) {
        return em.createQuery("SELECT p FROM Project p WHERE p.apiKey = :key AND p.client = :client", Project.class)
                .setParameter("key", key)
                .setParameter("client", client)
                .getResultList().stream().findFirst().orElseThrow(() -> new NotFoundException("Project not found"));
    }
}
