package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import recommendator.models.entities.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A cell is a single value from a {@link Row}. Each cell belongs to a {@link ColumnName} and has {@link Weight}.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"columnName", "row", "userPreference"})
public class Cell extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    @Length(min = 1, max = 255)
    private String value;

    @ManyToOne(optional = false)
    @NotNull
    private ColumnName columnName;

    @OneToOne
    private Row row;

    @ManyToOne
    private UserPreference userPreference;


}
