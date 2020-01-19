package services;

import models.Project;
import models.Row;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UserPreferenceRepository;

@Service
public class UserPreferenceService {

    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    public void createOrAdjust(Project project, User user, Row row) {
        row.getCells().forEach(cell -> {

        });
    }
}
