package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "columnName", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cell> cells;

    public void add(Cell cell) {
        if (cells == null) {
            cells = new ArrayList<>();
        }
        this.cells.add(cell);
    }

}
