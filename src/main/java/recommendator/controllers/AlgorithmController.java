package recommendator.controllers;

import recommendator.dto.GeneratedRecommendationDTO;
import recommendator.dto.RecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recommendator.services.algorithm.AlgorithmCore;

@RestController
@RequestMapping(path = "/recommendation")
public class AlgorithmController {

    private AlgorithmCore algorithmCore;

    @Autowired
    public AlgorithmController(AlgorithmCore algorithmCore) {
        this.algorithmCore = algorithmCore;
    }

    @GetMapping
    public ResponseEntity<GeneratedRecommendationDTO> getRecommendation(@RequestHeader("api-key") String apiKey, @RequestBody RecommendationDTO recommendationDTO) {
        return new ResponseEntity<>(algorithmCore.generateRecommendation(apiKey, recommendationDTO), HttpStatus.OK);
    }
}