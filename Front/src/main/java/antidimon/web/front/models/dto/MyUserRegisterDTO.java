package antidimon.web.front.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyUserRegisterDTO {

    @NotBlank(message = "username can't be empty")
    private String username;

    @NotBlank(message = "name can't be empty")
    private String name;

    @Min(value = 0, message = "min age is 0")
    @Max(value = 100, message = "max age is 100")
    private short age;

    @NotBlank(message = "email can't be empty")
    @Email(message = "must be email format")
    private String email;

    @NotBlank(message = "phone can't be empty")
    @Length(min = 11, max = 11, message = "Phone number must be like: 8********** (11 numbers)")
    private String phoneNumber;

    @NotBlank(message = "password can't be empty")
    private String password;


}
