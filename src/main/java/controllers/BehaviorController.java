package controllers;

import dto.BehaviorDTO;
import exceptions.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BehaviorService;

@RestController
@RequestMapping(path = "/behavior")
public class BehaviorController {

    private BehaviorService behaviorService;

    @Autowired
    public BehaviorController(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }

    @PostMapping
    public ResponseEntity<Response> submitFeedback(@RequestHeader("api-key") String apiKey, @RequestBody BehaviorDTO behaviorDTO) {
        return new ResponseEntity<>(behaviorService.add(apiKey, behaviorDTO), HttpStatus.ACCEPTED);
    }
}
