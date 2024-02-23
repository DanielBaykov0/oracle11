package baykov.daniel.oracle11.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private LocalDate birthDate;

    private String phoneNumber;

    @NotNull
    private Integer egn;

    private Boolean isActive;

    private Boolean isEmailVerified;

}
