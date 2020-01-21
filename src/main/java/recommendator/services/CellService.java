package recommendator.services;

import recommendator.models.entities.Cell;
import recommendator.models.entities.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.CellRepository;

import java.util.List;

@Service
public class CellService {

    private CellRepository cellRepository;

    @Autowired
    public CellService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    public List<Cell> getByRow(Row row) {
        return cellRepository.getByRow(row);
    }
}
