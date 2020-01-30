package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.shared.Weight;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A row is used to group multiple {@link Cell}'s that belong to each other.
 * These {@link Cell}'s together then form a single row.
 * Row is like a wrapper for {@link Cell} and other information such as {@link Behavior} and which project
 * it belongs to.
 */
// row is a reserved keyword in mysql 8+
@Entity(name = "Row")
@javax.persistence.Table(name = "`Row`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL)
    private List<Behavior> behaviors = new ArrayList<>();

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cell> cells = new ArrayList<>();
}
