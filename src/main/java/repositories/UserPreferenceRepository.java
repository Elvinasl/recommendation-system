package repositories;

import models.UserPreference;
import org.springframework.stereotype.Repository;

@Repository
public class UserPreferenceRepository extends DatabaseRepository<UserPreference> {

    public UserPreferenceRepository() {
        super(UserPreference.class);
    }
}
