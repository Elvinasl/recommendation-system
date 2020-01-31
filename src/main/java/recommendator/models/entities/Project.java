package recommendator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * In order to recommend something a project should be created. The project contains all the {@link Row}'s and
 * data. Only a {@link Client} can create, delete or update a project. In order to interacht with the project a
 * API-Key is required.
 */
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

    @Column(nullable = false, unique = true)
    private String apiKey;

    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Row> rows = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ColumnName> columnNames = new ArrayList<>();
}
