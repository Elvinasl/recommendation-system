package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.shared.Weight;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private String value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    private ColumnName columnName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Row> rows;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private UserPreference userPreference;

    public void addRow(Row row) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        this.rows.add(row);
    }
}
