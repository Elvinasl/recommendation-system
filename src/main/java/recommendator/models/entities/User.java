package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User can be seen as the end user of a {@link Client} his/her project.
 * Users are generating {@link Behavior} based on what they like/dislike from a specific project.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String externalUserId;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Behavior> behaviors;

    @ManyToOne
    private UserPreference userPreference;

}
