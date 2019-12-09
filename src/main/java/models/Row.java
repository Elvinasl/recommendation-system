package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    @NotNull
    private int weight = 50;

    @ManyToOne(optional = false)
    @Column(nullable = false)
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "row")
    private List<Behavior> behaviors;
}
