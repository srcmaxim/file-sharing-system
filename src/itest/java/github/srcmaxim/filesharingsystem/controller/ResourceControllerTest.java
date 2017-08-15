package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.ResourceService;
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
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
@Import(Config.class)
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
    public void findResourcesView() throws Exception {
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
    public void findResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource2)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void createResourceView() throws Exception {
        mvc.perform(get("/resources/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/createOrUpdate"))
                .andExpect(model().attribute("resource", new Resource()))
                .andExpect(model().attribute("type", is("create")));

        verify(resourceService, never()).findResource(null);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void createResource() throws Exception {
        File resource = new File(1L, "name", null, null);
        Folder parentFolder = new Folder(2L, null, null, null);
        when(resourceService.saveResource(resource, 2L, asList(1L, 2L), "file"))
                .thenReturn(new File(1L, "name", parentFolder, asList(user1, user2)));

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
                .saveResource(resource, 2L, asList(1L, 2L), "file");
        verifyNoMoreInteractions(resourceService);
    }

    public void updateUserView() throws Exception {
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
    public void updateResource() throws Exception {
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
                .saveResource(new File(1L, "name", null, null), 2L, asList(1L, 2L), "file");
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void deleteResourceView() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(get("/resources/{id}/delete", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("resources/findOneOrDelete"))
                .andExpect(model().attribute("resource", is(resource2)));

        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void deleteResource() throws Exception {
        when(resourceService.findResource(1L)).thenReturn(resource2);

        mvc.perform(post("/resources/{id}/delete", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/resources"));

        verify(resourceService, times(1)).deleteResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

}