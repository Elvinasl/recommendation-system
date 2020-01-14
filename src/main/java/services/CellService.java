package services;

import dto.CellDTO;
import models.Cell;
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


    public List<Cell> getCellsFromDB(List<CellDTO> cells) {
        return cellRepository.getCells(cells);
    }
}
