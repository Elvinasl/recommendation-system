package recommendator.repositories;

import org.springframework.transaction.annotation.Transactional;
import recommendator.models.entities.Client;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository extends DatabaseRepository<Client> {

    ClientRepository() {
        super(Client.class);
    }

    @Transactional
    public Client getByEmail(String email) {
        return (Client) em.createQuery("SELECT c FROM Client c WHERE c.email = :email")
                .setParameter("email", email)
                .getSingleResult();
    }
}
