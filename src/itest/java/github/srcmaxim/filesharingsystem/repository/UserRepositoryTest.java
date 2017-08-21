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

    private static User getUser() {
        User user = User.createNewUser("userLogin", "password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("user_user-user.user@user.user");
        user.setPhone("+3(099)-123-4567");
        return user;
    }

    @Test
    public void shouldFindAllImportedUsers() {
        List<User> users = repository.findAll();

        assertThat(users).isNotNull().isNotEmpty().doesNotContainNull().hasSize(3);
    }

    @Test
    public void shouldFindCreatedUser() {
        User user = getUser();
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertEquals(user.getLogin(), "userLogin");
        assertEquals(user.getPassword(), "password");
    }

    @Test
    public void shouldUpdateCreatedUser() {
        User user = getUser();
        repository.save(user);

        user = repository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertEquals(user.getEmail(), "user_user-user.user@user.user");
        assertEquals(user.getPhone(), "+3(099)-123-4567");
    }

    @Test
    public void shouldDeleteCreatedUser() {
        User user = getUser();
        repository.save(user);
        repository.delete(user.getId());

        user = repository.findOne(user.getId());

        assertThat(user).isNull();
    }

}