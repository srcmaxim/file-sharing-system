package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    private UserService service;
    private UserRepository repository;
    private List<User> userList;

    @Before
    public void init() throws Exception {
        repository = mock(UserRepository.class);
        service = new UserService(repository, null, NoOpPasswordEncoder.getInstance());
        userList = asList(
                User.createNewUser("Jack", "p1"),
                User.createNewUser("Jones", "p2")
        );
    }

    @Test
    public void shouldFindUsers() throws Exception {
        when(repository.findAll()).thenReturn(userList);

        List<User> users = service.findUsers();

        assertThat(users).contains(userList.toArray(new User[0]));
    }

    @Test
    public void shouldFindSavedUser() throws Exception {
        User user = mock(User.class);
        when(repository.findOne(1L)).thenReturn(user);
        when(user.getId()).thenReturn(1L).thenReturn(1L);

        service.saveUser(user);
        User savedUser = service.findUser(1L);

        assertThat(savedUser.getId()).isEqualTo(1L);
    }


    @Test
    public void shouldDeleteUser() throws Exception {
        User user = mock(User.class);
        when(repository.save(user)).thenReturn(user);
        when(repository.findOne(1L)).thenReturn(user).thenReturn(user).thenReturn(null);
        when(user.getId()).thenReturn(1L).thenReturn(1L);

        service.saveUser(user);
        User savedUser = service.findUser(1L);
        User deletedUser = service.deleteUser(savedUser.getId());
        User findedUser = service.findUser(1L);

        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(deletedUser.getId()).isEqualTo(1L);
        assertThat(findedUser).isNull();
    }

}