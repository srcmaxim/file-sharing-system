package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Authority;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.model.VerificationToken;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.repository.VerificationTokenRepository;
import github.srcmaxim.filesharingsystem.service.EmailService;
import github.srcmaxim.filesharingsystem.service.ServiceException;
import github.srcmaxim.filesharingsystem.service.UserPrincipalsService;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(DbConfig.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mvc;

    private User user;

    private static MockHttpSession session;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserPrincipalsService securityService;

    @MockBean
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeClass
    public static void setupSession() {
        session = new CustomHttpSession("user1", "12345qaz", asList(Authority.values()));
    }

    @Before
    public void setup() {
        user = User.createNewUser("user1", "12345qaz");
        user.setId(1L);
        user.setFirstName("UserOne");
        user.setLastName("UserOne");
        user.setPhone("+1(111)-111-1111");
        user.setEmail("user1@gmail.com");
    }

    @Test
    public void shouldMailVerificationTokenToUserAndSetUserNotVerifiedWhenRegistrationSuccess() throws Exception {
        doNothing().when(userService).principalsExist(any(), any());
        when(userService.createUserAccount(any(), any())).thenAnswer(invocation -> {
            try {
                invocation.callRealMethod();
            } catch (Throwable throwable) {
                // do nothing
            }
            return user;
        });
        doAnswer(invocation -> invocation.getArguments()[0]).when(userRepository).save((User) any());
        doAnswer(invocation -> invocation.getArguments()[0]).when(tokenRepository).save((VerificationToken) any());


        mvc.perform(post("/register").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", user.getId().toString())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("passwordConfirmation", user.getPassword())
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("email", user.getEmail())
                .param("phone", user.getPhone())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/info?type=*"));

        assertFalse(user.isEnabled());
    }

    @Test
    public void shouldRedirectErrorWhenRegistrationFailed() throws Exception {
        when(userService.createUserAccount(any(), any())).thenThrow(new ServiceException("mail-service-not-working"));


        mvc.perform(post("/register").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", user.getId().toString())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("passwordConfirmation", user.getPassword())
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("email", user.getEmail())
                .param("phone", user.getPhone())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/error?type=*"));
    }

}