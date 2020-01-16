package services;

import models.Project;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(User user) {
        return userRepository.add(user);
    }

    public User findByExternalIdAndProject(String externalUserId, Project project) {
        return userRepository.findByExternalIdAndProject(externalUserId, project);
    }

    public User findByExternalIdAndProjectOrNull(String externalUserId, Project project) {
        return userRepository.findByExternalIdAndProjectOrNull(externalUserId, project);
    }
}
