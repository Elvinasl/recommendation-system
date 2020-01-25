package recommendator.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.LoginDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import recommendator.services.ClientService;

import javax.validation.Valid;

@RestController
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @RequestMapping(path = "/register")
    public ResponseEntity<Response> add(@RequestBody @Valid LoginDTO loginDTO) {
        return new ResponseEntity<>(clientService.add(loginDTO), HttpStatus.CREATED);
    }
}
