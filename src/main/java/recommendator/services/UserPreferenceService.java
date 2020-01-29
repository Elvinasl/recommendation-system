package recommendator.services;

import recommendator.models.entities.Behavior;
import recommendator.models.entities.Row;
import recommendator.models.entities.User;
import recommendator.models.entities.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recommendator.repositories.UserPreferenceRepository;

/**
 * This service contains all the logic for everything that has something to do with UserPreferences.
 */
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

    /**
     * Creates or adjusts the userPreference for a specific user.
     * @param user to create or adjust for
     * @param row to create or adjust the cells for
     * @param behavior containing the liked/disliked info
     */
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
