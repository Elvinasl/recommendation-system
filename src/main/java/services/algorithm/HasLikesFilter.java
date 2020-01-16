package services.algorithm;

import models.Behavior;
import models.Project;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.RowRepository;
import services.BehaviorService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HasLikesFilter implements AlgorithmFilter {

    private RowRepository rowRepository;
    private BehaviorService behaviorService;

    @Autowired
    public HasLikesFilter(RowRepository rowRepository, BehaviorService behaviorService) {
        this.rowRepository = rowRepository;
        this.behaviorService = behaviorService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {


        User user = filtersData.getUser();
        Project project = filtersData.getProject();


        List<Behavior> likedContent = behaviorService.getBehaviorsByUserAndTypeAndProject(user, true, project);

//        if(likedContent.size() == 0) {
//            return generateDTO(getMostLikedContent(project));
//        }

        filtersData.setRows(new ArrayList<>());
        return filtersData;
    }



}
