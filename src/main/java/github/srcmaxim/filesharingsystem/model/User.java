package github.srcmaxim.filesharingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

@Data
@ToString(exclude = {"password", "resources"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "login", columnNames = "login"),
        @UniqueConstraint(name = "email", columnNames = "email"),
        @UniqueConstraint(name = "phone", columnNames = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "error.user.login.non-null")
    @Size(min = 4, max = 16, message = "error.user.login.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.login.pattern")
    private String login;
    @NotNull(message = "error.user.password.non-null")
    @Size(min = 6, max = 16, message = "error.user.password.size")
    @Pattern(regexp = "[A-z\\d]*", message = "error.user.password.pattern")
    private String password;
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

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_authority",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
            inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName="id")})
    private List<Authority> authorities = new ArrayList<>();

    @JsonIgnoreProperties("users")
    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST})
    private List<Resource> resources = new ArrayList<>();

    public User(String login, String password, List<Authority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }

    public static User createNewUser(String login, String password) {
        User user = new User(login, password, asList(new Authority(null , Authority.ROLE_USER)));
        List<User> userList = asList(user);
        user.getResources().add(new Folder("audio", null, userList));
        user.getResources().add(new Folder("video", null, userList));
        user.getResources().add(new Folder("image", null, userList));
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
