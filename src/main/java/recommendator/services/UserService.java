package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import recommendator.repositories.UserRepository;

/**
 * This service contains all the logic for everything that has something to do with Users.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds an {@link User} to the database.
     * @param user to add
     * @return added user
     */
    public User add(User user) {
        return userRepository.add(user);
    }

    /**
     * Gathers an {@link User} from the database by userId and a specific {@link Project}
     * @param externalUserId to search for
     * @param project the user should be part of
     * @return found user
     */
    public User findByExternalIdAndProjectOrNull(String externalUserId, Project project) {
        return userRepository.findByExternalIdAndProjectOrNull(externalUserId, project);
    }
}
