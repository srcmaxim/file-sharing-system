package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.Config;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserJsonController.class)
@Import(Config.class)
public class UserJsonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    List<User> users = asList(
            User.createNewUser("Jack", "p1"),
            User.createNewUser("Jones", "p2")
    );
    User user = users.get(0);

    @Test
    public void findUsers() throws Exception {
        when(userService.findUsers()).thenReturn(users);
        mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "  {" +
                        "    \"login\": \"Jack\"," +
                        "    \"role\": {\"name\": \"ROLE_USER\"}," +
                        "    \"resources\": [" +
                        "      {\"name\": \"audio\"}," +
                        "      {\"name\": \"video\"}," +
                        "      {\"name\": \"image\"} " +
                        "    ]}," +
                        "  {" +
                        "    \"login\": \"Jones\"," +
                        "    \"role\": {" +
                        "      \"name\": \"ROLE_USER\"" +
                        "    }," +
                        "    \"resources\": [" +
                        "      {\"name\": \"audio\"}," +
                        "      {\"name\": \"video\"}," +
                        "      {\"name\": \"image\"}" +
                        "    ]}" +
                        "]"));

        verify(userService, times(1)).findUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findUser() throws Exception {
        when(userService.findUser(1L))
                .thenReturn(user);
        mvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"login\": \"Jack\"," +
                        "  \"role\": {" +
                        "    \"name\": \"ROLE_USER\"" +
                        "  }," +
                        "  \"resources\": [" +
                        "    {\"name\": \"audio\"}," +
                        "    {\"name\": \"video\"}," +
                        "    {\"name\": \"image\"}" +
                        "  ]" +
                        "}"));

        verify(userService, times(1)).findUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUser() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"login\"," +
                        "  \"password\": \"password\"," +
                        "  \"firstName\": \"firstName\"," +
                        "  \"lastName\": \"lastName\"," +
                        "  \"email\": \"email-email.email@email.email\"," +
                        "  \"phone\": \"+30991234567\"" +
                        "}"))
                .andExpect(status().isOk());
        verify(userService).saveUser(eq(
                new User(null, "login", "password",
                        "firstName", "lastName",
                        "email-email.email@email.email", "+30991234567",
                        null, null)));
    }

    @Test
    public void updateUser() throws Exception {
        mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"login\"," +
                        "  \"password\": \"password\"," +
                        "  \"firstName\": \"firstName\"," +
                        "  \"lastName\": \"lastName\"," +
                        "  \"email\": \"email-email.email@email.email\"," +
                        "  \"phone\": \"+30991234567\"" +
                        "}"))
                .andExpect(status().isOk());
        verify(userService).saveUser(eq(
                new User(1L, "login", "password",
                        "firstName", "lastName",
                        "email-email.email@email.email", "+30991234567",
                        null, null)));
    }

    @Test
    public void deleteUser() throws Exception {
        mvc.perform(delete("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService).deleteUser(eq(1L));
    }

}