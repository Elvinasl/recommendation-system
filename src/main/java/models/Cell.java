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
public class Cell extends Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    @Length(min = 1, max = 255)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    private ColumnName columnName;

    @ManyToMany
    private List<Row> rows;

    @ManyToOne(optional = false)
    @NotNull
    private UserPreference userPreference;
}
