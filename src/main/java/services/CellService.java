package services;

import dto.CellDTO;
import models.Cell;
import models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CellRepository;

import java.util.List;

@Service
public class CellService {

    private CellRepository cellRepository;

    @Autowired
    public CellService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }


    public List<Cell> getCellsByCellDtoAndProject(List<CellDTO> cells, Project project) {
        return cellRepository.getCellsByCellDtoAndProject(cells, project);
    }
}
