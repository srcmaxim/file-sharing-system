package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(DbConfig.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private String userLogin = "userLogin";
    private String userPassword = "userPassword";
    private String userEmail = "user-user.user@user.user";
    private String userPhone = "+30991234567";

    @Test
    public void shouldFindAllImportedUsers() {
        List<User> users = repository.findAll();

        assertThat(users).isNotNull().isNotEmpty().doesNotContainNull().hasSize(3);
    }

    @Test
    public void shouldFindCreatedUser() {
        User user = User.createNewUser(userLogin, userPassword);
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertEquals(user.getLogin(), userLogin);
        assertEquals(user.getPassword(), userPassword);
    }

    @Test
    public void shouldUpdateCreatedUser() {
        User user = User.createNewUser(userLogin, userPassword);
        repository.save(user);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertEquals(user.getEmail(), userEmail);
        assertEquals(user.getPhone(), userPhone);
    }

    @Test
    public void shouldDeleteCreatedUser() {
        User user = User.createNewUser(userLogin, userPassword);
        repository.save(user);
        repository.delete(user.getId());

        user = repository.findOne(user.getId());

        assertThat(user).isNull();
    }

}