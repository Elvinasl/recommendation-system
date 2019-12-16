package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

@Service
public class TestService {
    private ClientRepository clientRepository;

    @Autowired
    public TestService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String test() {
        return clientRepository.getByEmail("test@ff.com").getId().toString();
    }
}
