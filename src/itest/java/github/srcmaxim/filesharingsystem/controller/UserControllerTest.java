package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(Config.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    List<User> users;
    User user;

    @Before
    public void setup() {
        users = asList(
                new User("Jack", "p1", null),
                new User("Jones", "p2", null)
        );
        user = users.get(0);
    }

    @Test
    public void findUsersView() throws Exception {
        when(userService.findUsers()).thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findAll"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", users));

        verify(userService, times(1)).findUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findOneOrDelete"))
                .andExpect(model().attribute("user", is(user)));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUserView() throws Exception {
        mvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(model().attribute("type", is("create")));

        verify(userService, never()).findUser(null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUser() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "login")
                .param("password", "password")
                .param("firstName", "firstName")
                .param("lastName", "lastName")
                .param("email", "email")
                .param("phone", "phone")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, times(1))
                .saveUser(new User(1L,
                        "login", "password",
                        "firstName", "lastName",
                        "email", "phone",
                        null, null));
        verifyNoMoreInteractions(userService);
    }

    public void updateUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/{id}/edit", 1L)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createOrUpdate"))
                .andExpect(model().attribute("user", hasItem(user)))
                .andExpect(model().attribute("type", is("update")));

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUser() throws Exception {
        mvc.perform(post("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("login", "login")
                .param("password", "password")
                .param("firstName", "firstName")
                .param("lastName", "lastName")
                .param("email", "email")
                .param("phone", "phone")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/1"));

        verify(userService, times(1))
                .saveUser(new User(1L,
                        "login", "password",
                        "firstName", "lastName",
                        "email", "phone",
                        null, null));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserView() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(get("/users/{id}/delete", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("users/findOneOrDelete"))
                .andExpect(model().attribute("user", is(user)));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUser() throws Exception {
        when(userService.findUser(1L)).thenReturn(user);

        mvc.perform(post("/users/{id}/delete", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).deleteUser(1L);
        verifyNoMoreInteractions(userService);
    }

}