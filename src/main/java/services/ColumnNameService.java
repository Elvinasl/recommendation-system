package services;

import models.ColumnName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ColumnNameRepository;

import java.util.List;

@Service
public class ColumnNameService implements DatabaseServiceInterface<ColumnName> {

    private final ColumnNameRepository columnNameRepository;

    @Autowired
    public ColumnNameService(ColumnNameRepository columnNameRepository) {
        this.columnNameRepository = columnNameRepository;
    }

    @Override
    public void add(ColumnName o) {
        columnNameRepository.add(o);
    }

    @Override
    public void update(ColumnName o) {
        columnNameRepository.update(o);
    }

    @Override
    public List<ColumnName> list() {
        return columnNameRepository.list();
    }

    @Override
    public ColumnName getById(int id) {
        return columnNameRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        columnNameRepository.remove(id);
    }
}
