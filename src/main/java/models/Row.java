package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.shared.Weight;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL)
    private List<Cell> cells = new ArrayList<>();
}
