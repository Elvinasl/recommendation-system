package recommendator.repositories;

import org.springframework.transaction.annotation.Transactional;
import recommendator.models.entities.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ClientRepository extends DatabaseRepository<Client> {

    ClientRepository() {
        super(Client.class);
    }

    /**
     * Gathers a {@link Client} from the database by email.
     * @param email to search for
     * @return Client with the given email
     * @throws NoResultException geths thrown whenever there is no {@link Client} with the given email
     */
    @Transactional
    public Client getByEmail(String email) throws NoResultException {
        return em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * Gathers all the {@link Client}'s that are activated (or not).
     * @param isActivated define if you want to search clients that are activated
     * @return all the {@link Client}'s that have been found
     */
    @Transactional
    public List<Client> getAllByActivated(boolean isActivated) {
        return em.createQuery("SELECT c FROM Client c WHERE c.activated = :isActivated", Client.class)
                .setParameter("isActivated", isActivated)
                .getResultList();
    }
}
