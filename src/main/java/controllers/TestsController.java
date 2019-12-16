package controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestsController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok( "Authenticated! Yaaahooo!");
    }
}
