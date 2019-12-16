package services;

import models.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CellRepository;

import java.util.List;

@Service
public class CellService implements DatabaseServiceInterface<Cell> {

    private final CellRepository cellRepository;

    @Autowired
    public CellService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    @Override
    public void add(Cell o) {
        cellRepository.add(o);
    }

    @Override
    public void update(Cell o) {
        cellRepository.update(o);
    }

    @Override
    public List<Cell> list() {
        return cellRepository.list();
    }

    @Override
    public Cell getById(int id) {
        return cellRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        cellRepository.remove(id);
    }
}
