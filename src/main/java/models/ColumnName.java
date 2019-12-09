package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnName extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    @Length(min = 1, max = 100)
    private String name;

    // TODO: combination of project and name has to be unique

    @ManyToOne(optional = false)
    @Column(nullable = false)
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "columnName")
    private List<Cell> cells;
}
