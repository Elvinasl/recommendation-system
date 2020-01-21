package recommendator.controllers;

import recommendator.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recommendator.services.ClientService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> add(@RequestBody @Valid Client client) {
        return ResponseEntity.ok(clientService.add(client));
    }
}
