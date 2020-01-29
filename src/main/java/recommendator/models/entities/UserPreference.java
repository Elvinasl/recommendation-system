package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.shared.Weight;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * UserPreference is a dynamic weight that is adjusted for each user individual based on there {@link Behavior}.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private Cell cell;

}
