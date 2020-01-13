package repositories;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import models.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class ClientRepository {

    @PersistenceContext(unitName = "entityManagerFactory")
    protected EntityManager em;

    @Transactional
    public Client getByEmail(String email) {
        return (Client) em.createQuery(
                "SELECT c FROM Client c WHERE c.email = :cEmail")
                .setParameter("cEmail", email)
                .getSingleResult();
    }

    @Transactional
    public Client getByApiKey(String apiKey) {
        return (Client) em.createQuery(
                "SELECT c FROM Client c, Project p WHERE p.apiKey = :apiKey AND p.client.id = c.id")
                .setParameter("apiKey", apiKey)
                .getSingleResult();
    }
}
