package services;

import models.Row;
import org.springframework.stereotype.Service;
import repositories.RowRepository;

import java.util.List;

@Service
public class RowService implements DatabaseServiceInterface<Row> {

    private final RowRepository rowRepository;

    public RowService(RowRepository rowRepository) {
        this.rowRepository = rowRepository;
    }

    @Override
    public void add(Row o) {
        rowRepository.add(o);
    }

    @Override
    public void update(Row o) {
        rowRepository.update(o);
    }

    @Override
    public List<Row> list() {
        return rowRepository.list();
    }

    @Override
    public Row getById(int id) {
        return rowRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        rowRepository.remove(id);
    }
}
