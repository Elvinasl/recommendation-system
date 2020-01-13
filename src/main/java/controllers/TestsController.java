package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.ClientRepository;

@RestController
public class TestsController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    @RequestMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok( "Authenticated! Yaaahooo!");
    }

    @GetMapping
    @RequestMapping("/api/test")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok( "API key works!");
    }
}
