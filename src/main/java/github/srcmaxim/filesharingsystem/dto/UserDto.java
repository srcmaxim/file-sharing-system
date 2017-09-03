package github.srcmaxim.filesharingsystem.dto;

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
@ToString(exclude = "password")
public class UserDto {

    @NotNull(message = "error.user.login.non-null")
    @Size(min = 4, max = 15, message = "error.user.login.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.login.pattern")
    private String login;
    @NotNull(message = "error.user.password.non-null")
    @Size(min = 6, max = 15, message = "error.user.password.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.password.pattern")
    private String password;

}
