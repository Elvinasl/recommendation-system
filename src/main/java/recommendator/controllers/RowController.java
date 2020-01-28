package recommendator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.ReturnObjectDTO;
import recommendator.services.RowService;

@RestController
public class RowController {

    private RowService rowService;

    @Autowired
    public RowController(RowService rowService) {
        this.rowService = rowService;
    }

    @GetMapping(path = "/projects/{api-key}/rows")
    public ResponseEntity<ReturnObjectDTO> getByApiKey(@PathVariable("api-key") String apiKey) {
        return ResponseEntity.ok(rowService.getByApiKey(apiKey));
    }

    // TODO: update row

    // TODO: delete a row
}
