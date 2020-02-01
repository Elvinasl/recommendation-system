package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * This DTO is used for a login request. It holds information to authenticate.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Email(message = "Incorrect email format!")
    private String email;
    @Length(min = 6, message = "Password has to be longer than 6 characters!")
    private String password;
}
