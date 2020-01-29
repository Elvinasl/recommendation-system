package recommendator.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A client can be seen as a customer from the service. Clients should not be confused with {@link User}'s
 * since a client is the person that can create/update/delete projects while an user is the one using
 * the client's projects.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @NotNull
    @JsonIgnore
    private String role;

    @NotNull
    @JsonIgnore
    private boolean activated = true;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Project> project = new ArrayList<>();
}
