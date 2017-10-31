package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Authority;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.model.VerificationToken;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private VerificationTokenRepository tokenRepository;
    private EmailService emailService;

    private List<User> userList;
    User user;
    BindingResult bindingResult = mock(BindingResult.class);

    @Before
    public void init() throws Exception {
        userRepository = mock(UserRepository.class);
        tokenRepository = mock(VerificationTokenRepository.class);
        emailService = mock(EmailService.class);

        userService = new UserService(userRepository, tokenRepository, emailService, NoOpPasswordEncoder.getInstance());
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
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.findUsers();

        assertThat(users).contains(userList.toArray(new User[0]));
    }

    @Test
    public void shouldFindSavedUser() throws Exception {
        User user = mock(User.class);
        when(userRepository.findOne(1L)).thenReturn(user);
        when(user.getId()).thenReturn(1L).thenReturn(1L);

        userService.saveUser(user);
        User savedUser = userService.findUser(1L);

        assertThat(savedUser.getId()).isEqualTo(1L);
    }


    @Test
    public void shouldDeleteUser() throws Exception {
        User user = mock(User.class);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findOne(1L)).thenReturn(user).thenReturn(user).thenReturn(null);
        when(user.getId()).thenReturn(1L).thenReturn(1L);

        userService.saveUser(user);
        User savedUser = userService.findUser(1L);
        User deletedUser = userService.deleteUser(savedUser.getId());
        User foundedUser = userService.findUser(1L);

        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(deletedUser.getId()).isEqualTo(1L);
        assertThat(foundedUser).isNull();
    }

    @Test
    public void shouldRegisterUserAutomaticallyWithFoldersAudioVideoImage() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userRepository.existsByLogin(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhone(any())).thenReturn(false);
        when(userRepository.save((User) any())).thenAnswer(returnFirstArgument());
        when(tokenRepository.save((VerificationToken) any())).thenAnswer(returnFirstArgument());
        doNothing().when(emailService).sendVerificationToken(any());

        user = userService.createUserAccount(user, bindingResult);

        assertThat(user.getAuthorities()).contains(Authority.ROLE_USER);
        assertThat(user.getResources()).extracting("name")
                .contains("audio", "video", "image");
    }

    private Answer<Object> returnFirstArgument() {
        return invocation -> invocation.getArguments()[0];
    }

}