package github.srcmaxim.filesharingsystem.model;

import lombok.*;

import java.util.List;

@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private List<Resource> resources;

}
