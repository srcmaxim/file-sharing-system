package github.srcmaxim.filesharingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Role role;

    @JsonIgnoreProperties("users")
    @ManyToMany(mappedBy = "users", cascade = {CascadeType.ALL})
    private List<Resource> resources = new ArrayList<>();

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public static User createNewUser(String login, String password) {
        User user = new User(login, password, new Role(null , Role.ROLE_USER));
        user.getResources().add(new Folder("audio", null, user));
        user.getResources().add(new Folder("video", null, user));
        user.getResources().add(new Folder("image", null, user));
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof User)) return false;

        User user = (User) o;
        return login != null ? login.equals(user.login) : user.login == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

}
