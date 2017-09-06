package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.ResourceService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
@Import(DbConfig.class)
public class ResourceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResourceService resourceService;
    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private List<Resource> resources;
    private Resource resource;

    private static MockHttpSession session;

    @BeforeClass
    public static void setupSession() {
        session = new CustomHttpSession("user1", "12345qaz", "ROLE_ADMIN", "ROLE_USER");
    }

    @Before
    public void setup() {
        user1 = User.createNewUser("Jack", "p1");
        user1.setId(1L);
        user1.setFirstName("Firstname");
        user1.setLastName("Lastname");
        user1.setPhone("+1(111)-111-1111");
        user1.setEmail("user1@gmail.com");
        user2 = User.createNewUser("John", "p2");
        user2.setId(2L);
        user1.setFirstName("Firstname");
        user1.setLastName("Lastname");
        user1.setPhone("+2(222)-222-2222");
        user1.setEmail("user1@gmail.com");
        resources = user1.getResources();
        resource = resources.get(0);
    }

    @Test
    public void shouldFindResourcesView() throws Exception {
        when(resourceService.findResources()).thenReturn(resources);

        mvc.perform(get("/resources").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findAll"))
                .andExpect(model().attribute("resources", hasSize(3)))
                .andExpect(model().attribute("resources", resources));

        verify(resourceService, times(1)).findResources();
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldFindResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource);

        mvc.perform(get("/resources/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldCreateResourceView() throws Exception {
        mvc.perform(get("/resources/create").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", new File()))
                .andExpect(model().attribute("type", is("create")));

        verify(resourceService, never()).findResource(null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldCreateResource() throws Exception {
        Resource resource = new Folder(1L, "name", null, null);
        when(resourceService.saveResource(resource, null))
                .thenReturn(resource);

        mvc.perform(post("/resources").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "name")
                .param("type", "folder")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources/1"));

        verify(resourceService, times(1))
                .saveResource(resource, null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldNotCreateResourceIfNotValid() throws Exception {
        String notValidName = "|\\|0T--valid..name";
        Resource resource = new Folder(1L, notValidName, null, null);

        mvc.perform(post("/resources").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", notValidName)
                .param("type", "folder")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", resource))
                .andExpect(model().attribute("type", is("create")));

        verify(resourceService, never())
                .saveResource(resource, null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldUpdateResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource);

        mvc.perform(get("/resources/{id}/edit", 1L).session(session)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", resource))
                .andExpect(model().attribute("type", is("update")));


        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldUpdateResource() throws Exception {
        Resource resource = new Folder(1L, "name", null, null);
        when(resourceService.updateResource(resource, null))
                .thenReturn(resource);

        mvc.perform(post("/resources/{id}", 1L).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "name")
                .param("type", "folder")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources/1"));

        verify(resourceService, times(1))
                .updateResource(resource, null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldNotUpdateResourceIfNotValid() throws Exception {
        String notValidName = "|\\|0T--valid..name";
        Resource resource = new Folder(1L, notValidName, null, null);

        mvc.perform(post("/resources/{id}", 1L).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", resource.getId().toString())
                .param("name", resource.getName())
                .param("type", "folder")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", resource))
                .andExpect(model().attribute("type", is("update")));

        verify(resourceService, never())
                .updateResource(resource, null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldDeleteResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource);

        mvc.perform(get("/resources/{id}/delete", 1L).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldDeleteResource() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource);

        mvc.perform(post("/resources/{id}/delete", 1L).session(session).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources"));

        verify(resourceService, times(1)).deleteResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

}