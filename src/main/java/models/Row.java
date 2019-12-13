package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.shared.Weight;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Behavior> behaviors;

    @ManyToMany(mappedBy = "rows", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cell> cells;

    public void addCell(Cell cell) {
        if (cells == null) {
            cells = new ArrayList<>();
        }
        this.cells.add(cell);
    }
}
