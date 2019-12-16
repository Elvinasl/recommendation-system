package services;

import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UserRepository;

import java.util.List;

@Service
public class UserService implements DatabaseServiceInterface<User> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User o) {
        userRepository.add(o);
    }

    @Override
    public void update(User o) {
        userRepository.update(o);
    }

    @Override
    public List<User> list() {
        return userRepository.list();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        userRepository.remove(id);
    }
}
