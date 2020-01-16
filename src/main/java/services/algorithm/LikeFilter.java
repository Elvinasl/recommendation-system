package services.algorithm;

import models.Behavior;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.RowRepository;

import java.util.List;

public class LikeFilter implements AlgorithmFilter {

    private RowRepository rowRepository;

    @Autowired
    public LikeFilter(RowRepository rowRepository) {
        this.rowRepository = rowRepository;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {
        if (user == null) {
            return generateDTO(getMostLikedContent(project));
        }

        List<Behavior> likedContent = behaviorService.getBehaviorsByUserAndTypeAndProject(user, true, project);

        if(likedContent.size() == 0) {
            return generateDTO(getMostLikedContent(project));
        }
    }

    private List<Row> getMostLikedContent(Project project) {
        // get most liked movies
        return rowRepository.findMostLiked(project, recommendationDTO.getAmount());
    }


}
