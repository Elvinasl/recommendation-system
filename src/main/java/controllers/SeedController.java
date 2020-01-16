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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public ResponseEntity<String> seedBehavior(@RequestHeader("api-key") String apiKey, @PathVariable int times) {

        Project project = projectService.getByApiKey(apiKey);
        List<Row> rows = project.getRows();
        if (rows == null) rows = new ArrayList<>();
        int numberOfRows = rows.size();
        System.out.println(numberOfRows);


        Faker faker = new Faker();
        for (int i = 0; i < times; i++) {
            Row row = rows.get(faker.random().nextInt(0, numberOfRows - 1));
            BehaviorDTO behaviorDTO = new BehaviorDTO();
            behaviorDTO.setUserId(String.valueOf(faker.random().nextInt(0, times / 10 - 1)));
            List<CellDTO> behaviorDTOCells = behaviorDTO.getCells();
            if (behaviorDTOCells == null) behaviorDTOCells = new ArrayList<>();

            List<Cell> cells = row.getCells();
            for (int j = 0; j < cells.size(); j++) {
                Cell cell = cells.get(j);
                behaviorDTOCells.add(new CellDTO(cell.getColumnName().getName(), cell.getValue()));
            }
            behaviorDTO.setCells(behaviorDTOCells);
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

        List<DatasetRowDTO> rows = datasetDTO.getRows();
        if (rows == null) rows = new ArrayList<>();


        for (int i = 0; i < times; i++) {

            DatasetRowDTO datasetRowDTO = new DatasetRowDTO();
            List<DatasetCellDTO> cells = new ArrayList<>();

            Book book = faker.book();
            cells.add(this.datasetCellDTO(50, "author", book.author()));
            cells.add(this.datasetCellDTO(50, "genre", book.genre()));
            cells.add(this.datasetCellDTO(50, "publisher", book.publisher()));
            cells.add(this.datasetCellDTO(50, "title", book.title()));

            datasetRowDTO.setCells(cells);

            rows.add(datasetRowDTO);
        }
        datasetDTO.setRows(rows);

        return new ResponseEntity<>(projectService.seedDatabase(apiKey, datasetDTO), HttpStatus.CREATED);
    }

    private DatasetCellDTO datasetCellDTO(int weight, String columnName, String value) {
        DatasetCellDTO cell = new DatasetCellDTO(weight);
        cell.setColumnName(columnName);
        cell.setValue(value);
        return cell;
    }
}
