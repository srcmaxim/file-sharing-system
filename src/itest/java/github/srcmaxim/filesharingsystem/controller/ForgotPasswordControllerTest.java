package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Authority;
import github.srcmaxim.filesharingsystem.service.EmailService;
import github.srcmaxim.filesharingsystem.service.ServiceException;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
@Import({Config.class, MailConfig.class, DbConfig.class, SecurityConfig.class, WebConfig.class})
public class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;

    @Autowired
    private UserService userService;

    private static MockHttpSession session;

    @BeforeClass
    public static void setupSession() {
        session = new CustomHttpSession("user1", "12345qaz", asList(Authority.values()));
    }

    @Test
    public void shouldNotChangePasswordIfEmailNotValid() throws Exception {
        String oldPassword = userService.findUser(1L).getPassword();

        mvc.perform(get("/forgot-password").session(session)
                .param("email", "not..valid..email")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"))
                .andExpect(model().attribute("email", "not..valid..email"))
                .andExpect(model().attribute("send", false))
                .andExpect(model().attribute("error", "error.user.email.non-valid"));

        String newPassword = userService.findUser(1L).getPassword();
        assertEquals(newPassword, oldPassword);
        verify(emailService, never()).sendPasswordTo(any());
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldNotChangePasswordIfEmailNotExist() throws Exception {
        String oldPassword = userService.findUser(1L).getPassword();

        mvc.perform(get("/forgot-password").session(session)
                .param("email", "valid@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"))
                .andExpect(model().attribute("email", "valid@email.com"))
                .andExpect(model().attribute("send", false))
                .andExpect(model().attribute("error", "error.user.email.no-email"));

        String newPassword = userService.findUser(1L).getPassword();
        assertEquals(newPassword, oldPassword);
        verify(emailService, never()).sendPasswordTo(any());
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldNotChangePasswordIfServiceError() throws Exception {
        doThrow(new ServiceException("forgot.error"))
                .when(emailService).sendPasswordTo(any());
        String oldPassword = userService.findUser(1L).getPassword();

        mvc.perform(get("/forgot-password").session(session)
                .param("email", "user1@gmail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"))
                .andExpect(model().attribute("email", "user1@gmail.com"))
                .andExpect(model().attribute("send", false))
                .andExpect(model().attribute("error", "forgot.error"));

        String newPassword = userService.findUser(1L).getPassword();
        assertEquals(newPassword, oldPassword);
        verify(emailService, times(1)).sendPasswordTo(any());
        verifyNoMoreInteractions(emailService);
    }

    @Test
    @Transactional
    public void shouldChangePasswordForGivenUserEmail() throws Exception {
        doNothing().when(emailService).sendPasswordTo(any());
        String oldPassword = userService.findUser(1L).getPassword();

        mvc.perform(get("/forgot-password").session(session)
                .param("email", "user1@gmail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"))
                .andExpect(model().attribute("email", "user1@gmail.com"))
                .andExpect(model().attribute("send", true));

        String newPassword = userService.findUser(1L).getPassword();
        assertNotEquals(newPassword, oldPassword);
        verify(emailService, times(1)).sendPasswordTo(any());
        verifyNoMoreInteractions(emailService);
    }

}