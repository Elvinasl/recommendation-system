package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This model/entity is used for liking/disliking (behavior).
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Behavior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private boolean liked;

    @ManyToOne(optional = false)
    @NotNull
    private Row row;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
