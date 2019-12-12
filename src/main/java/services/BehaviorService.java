package services;

import models.Behavior;
import org.springframework.stereotype.Service;
import repositories.BehaviorRepository;

import java.util.List;

@Service
public class BehaviorService implements DatabaseServiceInterface<Behavior> {

    private final BehaviorRepository behaviorRepository;

    public BehaviorService(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }

    @Override
    public void add(Behavior o) {
        behaviorRepository.add(o);
    }

    @Override
    public void update(Behavior o) {
        behaviorRepository.update(o);
    }

    @Override
    public List<Behavior> list() {
        return behaviorRepository.list();
    }

    @Override
    public Behavior getById(int id) {
        return behaviorRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        behaviorRepository.remove(id);
    }
}
