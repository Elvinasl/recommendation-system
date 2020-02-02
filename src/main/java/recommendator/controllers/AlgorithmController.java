package recommendator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.GeneratedRecommendationDTO;
import recommendator.services.algorithm.AlgorithmCore;

@RestController
@RequestMapping(path = "/recommendation")
public class AlgorithmController {

    private AlgorithmCore algorithmCore;

    @Autowired
    public AlgorithmController(AlgorithmCore algorithmCore) {
        this.algorithmCore = algorithmCore;
    }

    @GetMapping(path = "user/{external-user-id}")
    public ResponseEntity<GeneratedRecommendationDTO> getRecommendation(@RequestHeader("api-key") String apiKey, @PathVariable("external-user-id") String externalUserId, @RequestParam("amount") int amount) {

        return new ResponseEntity<>(algorithmCore.generateRecommendation(apiKey, externalUserId, amount), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GeneratedRecommendationDTO> getRecommendation(@RequestHeader("api-key") String apiKey, @RequestParam(value = "amount", defaultValue = "10") int amount) {
        return new ResponseEntity<>(algorithmCore.generateRecommendation(apiKey, null, amount), HttpStatus.OK);
    }
}
