package repositories;


import models.Behavior;
import org.springframework.stereotype.Repository;

@Repository
public class BehaviorRepository extends DatabaseRepository<Behavior> {

    BehaviorRepository() {
        super(Behavior.class);
    }

}
