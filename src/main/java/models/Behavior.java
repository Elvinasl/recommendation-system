package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Behavior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private boolean liked;

    @ManyToOne(optional = false)
    @Column(nullable = false)
    @NotNull
    private Row row;

    @ManyToOne(optional = false)
    @Column(nullable = false)
    @NotNull
    private User user;
}