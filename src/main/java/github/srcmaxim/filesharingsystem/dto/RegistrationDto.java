package github.srcmaxim.filesharingsystem.dto;

import github.srcmaxim.filesharingsystem.annotation.Equals;
import github.srcmaxim.filesharingsystem.model.GenericUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "passwordConfirmation"})
@Equals(firstField = "password", secondField = "passwordConfirmation",
        message = "error.user.password.not-equals")
public class RegistrationDto implements GenericUser {

    @NotNull(message = "error.user.login.non-null")
    @Size(min = 4, max = 16, message = "error.user.login.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.login.pattern")
    private String login;
    @NotNull(message = "error.user.password.non-null")
    @Size(min = 6, max = 16, message = "error.user.password.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.password.pattern")
    private String password;
    private String passwordConfirmation;
    @NotNull(message = "error.user.first-name.non-null")
    @Size(min = 2, max = 16, message = "error.user.first-name.size")
    @Pattern(regexp = "\\p{Alpha}*", message = "error.user.first-name.pattern")
    private String firstName;
    @NotNull(message = "error.user.last-name.non-null")
    @Size(min = 2, max = 16, message = "error.user.last-name.size")
    @Pattern(regexp = "\\p{Alpha}*", message = "error.user.last-name.pattern")
    private String lastName;
    @NotNull(message = "error.user.email.non-null")
    @Pattern(regexp = "[A-z0-9]+(\\-[A-z0-9]+|\\.[A-z0-9]+|\\_[A-z0-9]+)*@[A-z0-9]{2,}(\\.[A-z]{2,})+",
            message = "error.user.email.non-valid")
    private String email;
    @NotNull(message = "error.user.phone.non-null")
    @Pattern(regexp = "\\+\\d\\(\\d{3}\\)\\-\\d{3}\\-\\d{4}",
            message = "error.user.phone.non-valid")
    private String phone;

}
