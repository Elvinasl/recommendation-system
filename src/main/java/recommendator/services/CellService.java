package recommendator.services;

import recommendator.dto.CellDTO;
import recommendator.models.entities.Cell;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.CellRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CellService {

    private CellRepository cellRepository;
    private ColumnNameService columnNameService;

    @Autowired
    public CellService(CellRepository cellRepository, ColumnNameService columnNameService) {
        this.cellRepository = cellRepository;
        this.columnNameService = columnNameService;
    }

    public List<Cell> getByRow(Row row) {
        return cellRepository.getByRow(row);
    }

    private Cell getById(long id) {
        return cellRepository.getById(id);
    }

    /**
     * Updates cell values
     *
     * @param cells list of CellDTO
     * @return list of updated cells
     */
    public List<Cell> updateCellValues(List<CellDTO> cells) {
        return cells.stream()
            .map(cellDTO -> {
            Cell cell = getById(cellDTO.getId());
            cell.setValue(cellDTO.getValue());
            return cellRepository.update(cell);
        }).collect(Collectors.toList());
    }

    /**
     * Creates a cell
     * @param cellDTO Cell dto with values
     * @param project Project for this row
     * @param row persisted row
     * @return
     */
    public Cell create(CellDTO cellDTO, Project project, Row row) {
        Cell cell = new Cell();
        cell.setValue(cellDTO.getValue());
        cell.setColumnName(columnNameService.getByNameAndProject(cellDTO.getColumnName(), project));
        cell.setRow(row);
        return cellRepository.add(cell);
    }
}
