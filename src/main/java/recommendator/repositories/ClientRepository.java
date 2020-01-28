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

    @Transactional
    public Client getByEmail(String email) throws NoResultException {
        return (Client) em.createQuery("SELECT c FROM Client c WHERE c.email = :email")
                .setParameter("email", email)
                .getSingleResult();
    }

    @Transactional
    public List<Client> getAllByActivated(boolean isActivated) {
        return em.createQuery("SELECT c FROM Client c WHERE c.activated = :isActivated")
                .setParameter("isActivated", isActivated)
                .getResultList();
    }
}
