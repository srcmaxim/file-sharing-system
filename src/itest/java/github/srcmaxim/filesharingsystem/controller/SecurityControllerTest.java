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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    @Ignore
    public void shouldSetUserEnabledAndDeleteVerificationTokenWhenTokenOk() throws Exception {
        VerificationToken token = new VerificationToken(user);

        when(userService.findVerificationToken(token.getToken())).thenReturn(token);
        doNothing().when(userService).deleteVerificationToken(token);
        doCallRealMethod().when(userService).completeRegistration(token.getToken());

        mvc.perform(get("/verification").session(session)
                .param("token", token.getToken())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));

        assertTrue(user.isEnabled());
    }

    @Test
    public void shouldRedirectErrorWhenNoSuchToken() throws Exception {
        String token = "aaabbb";

        when(userService.findVerificationToken(token)).thenReturn(null);
        doCallRealMethod().when(userService).completeRegistration(token);

        mvc.perform(get("/verification").session(session)
                .param("token", token)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/error?type=no-such-token"));

        assertFalse(user.isEnabled());
    }

    @Test
    public void shouldRedirectErrorWhenTokenExpired() throws Exception {
        VerificationToken token = getExpiredToken();

        when(userService.findVerificationToken(token.getToken())).thenReturn(token);
        doNothing().when(userService).deleteVerificationToken(token);
        doCallRealMethod().when(userService).completeRegistration(token.getToken());

        mvc.perform(get("/verification").session(session)
                .param("token", token.getToken())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/error?type=expired-token"));

        assertFalse(user.isEnabled());
    }

    private VerificationToken getExpiredToken() throws NoSuchFieldException, IllegalAccessException {
        VerificationToken token = new VerificationToken(user);
        Field expiryDate = VerificationToken.class.getDeclaredField("expiryDate");
        expiryDate.setAccessible(true);
        expiryDate.set(token, calculateExpiryDate());
        return token;
    }

    private Instant calculateExpiryDate() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        return utc.minusDays(1).toInstant();
    }

    @Test
    public void shouldRedirectErrorWhenUserNotEnabled() throws Exception {
        doThrow(new AccountExpiredException("email-not-verified"))
                .when(securityService).loadUserByUsername(any());

        mvc.perform(post("/login").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", user.getLogin())
                .param("password", user.getPassword())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/info?type=email-not-verified"));
    }

}