package repositories;


import models.entities.Client;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository extends DatabaseRepository<Client> {

    ClientRepository() {
        super(Client.class);
    }

}
