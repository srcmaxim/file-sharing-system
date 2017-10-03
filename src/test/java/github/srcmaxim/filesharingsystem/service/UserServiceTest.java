package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Authority;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    private UserService service;
    private UserRepository repository;
    private List<User> userList;
    User user;
    BindingResult bindingResult = mock(BindingResult.class);

    @Before
    public void init() throws Exception {
        repository = mock(UserRepository.class);
        service = new UserService(repository, null, null, NoOpPasswordEncoder.getInstance());
        userList = asList(
                User.createNewUser("Jack", "p1"),
                User.createNewUser("Jones", "p2")
        );
        user = new User(null,
                "user4", "12345qaz",
                "Userfourname", "Userfourname",
                "user4@gmail.com", "+4(444)-444-4444",
                true,
                null, null);
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
        User foundedUser = service.findUser(1L);

        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(deletedUser.getId()).isEqualTo(1L);
        assertThat(foundedUser).isNull();
    }

    @Test
    public void shouldRegisterUserAutomaticallyWithFoldersAudioVideoImage() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(repository.existsByLogin(any())).thenReturn(false);
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.existsByPhone(any())).thenReturn(false);
        when(repository.save((User) any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        user = service.createUserAccount(user, bindingResult);

        assertThat(user.getAuthorities()).contains(Authority.ROLE_USER);
        assertThat(user.getResources()).extracting("name")
                .contains("audio", "video", "image");
    }

}