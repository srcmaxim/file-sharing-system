package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(DbConfig.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setup() {
        String password = new StandardPasswordEncoder().encode("userPassword");
        User user = User.createNewUser("userLogin", password);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("user_user-user.user@user.user");
        user.setPhone("+3(099)-123-4567");
        this.user = user;
    }

    @Test
    public void shouldFindAllImportedUsers() {
        List<User> users = repository.findAll();

        assertThat(users).isNotNull().isNotEmpty().doesNotContainNull().hasSize(3);
    }

    @Test
    public void shouldFindCreatedUser() {
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
    }

    @Test
    public void shouldUpdateCreatedUser() {
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
    }

    @Test
    public void shouldDeleteCreatedUser() {
        repository.save(user);
        repository.delete(user.getId());

        user = repository.findOne(user.getId());

        assertThat(user).isNull();
    }

}