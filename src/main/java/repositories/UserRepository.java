package repositories;


import models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends DatabaseRepository<User> {

    UserRepository() {
        super(User.class);
    }

}
