package controllers;

import models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ClientService;

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
    // TODO: pass LoginViewModel
    public ResponseEntity<Client> add(@RequestBody @Valid Client client) {
        return ResponseEntity.ok(clientService.add(client));
    }
}
