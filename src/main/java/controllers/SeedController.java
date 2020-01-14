package controllers;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import dto.*;
import exceptions.responses.Response;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BehaviorService;
import services.ProjectService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/seed")
public class SeedController {

    private final BehaviorService behaviorService;

    private final ProjectService projectService;

    public SeedController(BehaviorService behaviorService, ProjectService projectService) {
        this.behaviorService = behaviorService;
        this.projectService = projectService;
    }


    @GetMapping(path = "behavior/{times}")
    public ResponseEntity<String> seedBehavior(@RequestHeader("api-key") String apiKey, @PathVariable int times) {

        Project project = projectService.getByApiKey(apiKey);
        List<Row> rows = project.getRows();
        int numberOfRows = rows.size();

        Faker faker = new Faker();
        for (int i = 0; i < times; i++) {
            Row row = rows.get(faker.random().nextInt(0, numberOfRows));
            BehaviorDTO behaviorDTO = new BehaviorDTO();
            List<Cell> cells = row.getCells();
            for (int j = 0; j < cells.size(); j++) {
                Cell cell = cells.get(j);
                behaviorDTO.getCells().add(new CellDTO(cell.getColumnName().getName(), cell.getValue()));
            }
            // Two times bool() because the first returns 'bool' and the second 'boolean'
            behaviorDTO.setLiked(faker.bool().bool());
            behaviorService.add(apiKey, behaviorDTO);
        }

        return new ResponseEntity<String>("", HttpStatus.CREATED);
    }

    @GetMapping(path = "data/{times}")
    public ResponseEntity<Response> seedData(@RequestHeader("api-key") String apiKey, @PathVariable int times) {

        Faker faker = new Faker();

        DatasetDTO datasetDTO = new DatasetDTO();
        List<ColumnName> columns = new ArrayList<>();

        ColumnName author = new ColumnName();
        author.setName("author");
        columns.add(author);

        ColumnName genre = new ColumnName();
        genre.setName("genre");
        columns.add(genre);

        ColumnName publisher = new ColumnName();
        publisher.setName("publisher");
        columns.add(publisher);

        ColumnName title = new ColumnName();
        title.setName("title");
        columns.add(title);

        datasetDTO.setColumns(columns);

        List<RowDTO> rows = datasetDTO.getRows();
        if (rows == null) rows = new ArrayList<>();


        for (int i = 0; i < times; i++) {

            RowDTO rowDTO = new RowDTO();
            List<DatasetCellDTO> cells = new ArrayList<>();

            Book book = faker.book();
            cells.add(DatasetCellDTO.create(50, "author", book.author()));
            cells.add(DatasetCellDTO.create(50, "genre", book.genre()));
            cells.add(DatasetCellDTO.create(50, "publisher", book.publisher()));
            cells.add(DatasetCellDTO.create(50, "title", book.title()));

            rowDTO.setCells(cells);

            rows.add(rowDTO);
        }
        datasetDTO.setRows(rows);

        return new ResponseEntity<>(projectService.seedDatabase(apiKey, datasetDTO), HttpStatus.CREATED);
    }
}
