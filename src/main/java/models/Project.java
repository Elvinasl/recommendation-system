package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dto.Dataset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    @Length(min = 2, max = 45)
    private String name;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String apiKey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    private Client client;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Row> rows = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ColumnName> columnNames = new ArrayList<>();

    /**
     * Add row to the project list of rows
     *
     * @param row Row to add to the list
     */
    public void addRow(Row row) {
        this.rows.add(row);
    }
}
