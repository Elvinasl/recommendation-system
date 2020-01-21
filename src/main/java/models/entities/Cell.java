package models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import models.entities.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
