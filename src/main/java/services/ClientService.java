package services;

import models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

import java.util.List;

@Service
public class ClientService implements DatabaseServiceInterface<Client> {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void add(Client o) {
        clientRepository.add(o);
    }

    @Override
    public void update(Client o) {
        clientRepository.update(o);
    }

    @Override
    public List<Client> list() {
        return clientRepository.list();
    }

    @Override
    public Client getById(int id) {
        return clientRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        clientRepository.remove(id);
    }
}
