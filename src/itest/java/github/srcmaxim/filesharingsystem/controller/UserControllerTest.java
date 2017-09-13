package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(DbConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private List<User> users;
    private User user;
    private static MockHttpSession session;

    @BeforeClass
    public static void setupSession() {
        session = new CustomHttpSession("user1", "12345qaz", "ROLE_ADMIN", "ROLE_USER");
    }

    @Before
    public void setup() {
        users = asList(
                new User(1L, "user1", "12345qaz",
                        "Firstname", "Lastname",
                        "+1(111)-111-1111", "user1@gmail.com",
                        null, null),
                new User(2L, "user2", "12345qaz",
                        "Firstname", "Lastname",
                        "+2(222)-222-2222", "user2@gmail.com",
                        null, null)
        );
        user = users.get(0);
    }

    @Test
    public void shouldFindUsersView() throws Exception {
        when(userService.findUsers()).thenReturn(users);

        mvc.perform(get("/users").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findAll"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", users));

        verify(userService, times(1)).findUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldFindUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findOneOrDelete"))
                .andExpect(model().attribute("user", is(user)));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldCreateUserView() throws Exception {
        mvc.perform(get("/users/create").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(model().attribute("type", is("create")));

        verify(userService, never()).findUser(null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        mvc.perform(post("/users").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "user1")
                .param("password", "12345qaz")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("email", "user1@gmail.com")
                .param("phone", "+1(111)-111-1111")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, times(1)).saveUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotCreateUserIfNotValid() throws Exception {
        User user = new User(1L, "user1", "12345qaz",
                "Bad name", "Bad name",
                "Bad email", "Bad phone",
                null, null);
        mvc.perform(post("/users").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", user.getId().toString())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("email", user.getEmail())
                .param("phone", user.getPhone())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("type", is("create")));

        verify(userService, never()).saveUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotCreateUserIfNoCsrfToken() throws Exception {
        mvc.perform(post("/users").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "user1")
                .param("password", "12345qaz")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("email", "user1@gmail.com")
                .param("phone", "+1(111)-111-1111")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, never()).saveUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldUpdateUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/{id}/edit", 1L).session(session)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("type", is("update")));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        when(userService.updateUser(user)).thenReturn(user);

        mvc.perform(post("/users/{id}", 1L).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "user1")
                .param("password", "12345qaz")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("email", "user1@gmail.com")
                .param("phone", "+1(111)-111-1111")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, times(1)).updateUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotUpdateUserIfNotValid() throws Exception {
        User user = new User(1L, "user1", "12345qaz",
                "Bad name", "Bad name",
                "Bad email", "Bad phone",
                null, null);

        mvc.perform(post("/users/{id}", 1L).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", user.getId().toString())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("email", user.getEmail())
                .param("phone", user.getPhone())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("type", is("update")));

        verify(userService, never()).updateUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotUpdateUserIfNoCsrfToken() throws Exception {
        mvc.perform(post("/users/{id}", 1L).session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "user1")
                .param("password", "12345qaz")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("email", "user1@gmail.com")
                .param("phone", "+1(111)-111-1111")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, never()).updateUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldDeleteUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/{id}/delete", 1L).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findOneOrDelete"))
                .andExpect(model().attribute("user", is(user)));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(post("/users/{id}/delete", 1L).session(session).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).deleteUser(1L);
        verifyNoMoreInteractions(userService);
    }

}