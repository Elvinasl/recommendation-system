package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * ColumnNames describe the labels that are used for predictions and can be compared with column names in spreadsheets.
 * Each {@link Row} exists of one or more ColumnNames (Eg. "title", "genre" and "author").
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "name", "project_id" } ) } )
public class ColumnName extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, name = "name")
    @Length(min = 1, max = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "columnName", cascade = CascadeType.ALL)
    private List<Cell> cells = new ArrayList<>();
}
