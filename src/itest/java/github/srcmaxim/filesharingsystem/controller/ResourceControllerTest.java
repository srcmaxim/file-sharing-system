package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.DbConfig;
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

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
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

    User user1;
    User user2;
    List<Resource> resources1;
    Resource resource2;

    @Before
    public void setup() {
        user1 = User.createNewUser("Jack", "p1");
        user1.setId(1L);
        user2 = User.createNewUser("John", "p2");
        user2.setId(2L);
        resources1 = user1.getResources();
        resource2 = resources1.get(0);
    }

    @Test
    public void shouldFindResourcesView() throws Exception {
        when(resourceService.findResources()).thenReturn(resources1);

        mvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findAll"))
                .andExpect(model().attribute("resources", hasSize(3)))
                .andExpect(model().attribute("resources", resources1));

        verify(resourceService, times(1)).findResources();
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldFindResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource2)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldCreateResourceView() throws Exception {
        mvc.perform(get("/resources/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", new File()))
                .andExpect(model().attribute("type", is("create")));

        verify(resourceService, never()).findResource(null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldCreateResource() throws Exception {
        File resource = new File(1L, "name", null, null);
        when(resourceService.saveResource(resource))
                .thenReturn(new File(1L, "name", null, null));

        mvc.perform(post("/resources")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "name")
                .param("parentId", "2")
                .param("userIds", "1, 2")
                .param("type", "file")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources/1"));

        verify(resourceService, times(1))
                .saveResource(resource);
        verifyNoMoreInteractions(resourceService);
    }

    public void shouldUpdateUserView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(get("/resources/{id}/edit", 1L)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", hasItem(resource2)))
                .andExpect(model().attribute("type", is("update")));

        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldUpdateResource() throws Exception {
        when(resourceService.findResource(2L)).thenReturn(resource2);
        when(userService.findUsers(asList(1L))).thenReturn(asList(user1));

        mvc.perform(post("/resources/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "name")
                .param("parentId", "2")
                .param("userIds", "1, 2")
                .param("type", "file")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources/1"));

        verify(resourceService, times(1))
                .saveResource(new File(1L, "name", null, null));
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldDeleteResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(get("/resources/{id}/delete", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource2)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void shouldDeleteResource() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(post("/resources/{id}/delete", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources"));

        verify(resourceService, times(1)).deleteResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

}