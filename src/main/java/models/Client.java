package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @JsonIgnore
    @NotEmpty
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy="client")
    private List<Project> project;

    @NotNull
    @JsonIgnore
    private String role;

    @NotNull
    @JsonIgnore
    private String permissions = "";

    @NotNull
    @JsonIgnore
    private boolean activated = true;

    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}
