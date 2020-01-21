package services;

import models.entities.Behavior;
import models.entities.Row;
import models.entities.User;
import models.entities.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserPreferenceRepository;

@Service
@PropertySource("classpath:algorithm.properties")
public class UserPreferenceService {

    private UserPreferenceRepository userPreferenceRepository;

    @Value("${default_weight_change}")
    private int DEFAULT_CHANGE;

    @Autowired
    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Transactional
    public void createOrAdjust(User user, Row row, Behavior behavior) {
        row.getCells().forEach(cell -> {
            UserPreference userPreference = userPreferenceRepository.getByCellOrNull(cell);
            if(userPreference == null) {
                userPreference = new UserPreference();
                userPreference.setUser(user);
                userPreference.setCell(cell);
            }

            // if user liked this cell, we will increment weight, if not we will decrease it
            int change = behavior.isLiked() ? DEFAULT_CHANGE : DEFAULT_CHANGE * -1;
            int currentWeight = userPreference.getWeight();
            // max weight is 100
            int finalWeight = currentWeight < 100 ? currentWeight + change : currentWeight;
            userPreference.setWeight(finalWeight);
            userPreferenceRepository.update(userPreference);
        });
    }
}
