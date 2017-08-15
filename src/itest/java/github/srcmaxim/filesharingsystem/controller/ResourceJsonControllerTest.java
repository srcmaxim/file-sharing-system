package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.service.ResourceService;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
@Import(Config.class)
public class ResourceJsonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResourceService resourceService;

    List<Resource> resources = asList(
            new Resource("audio", null, null),
            new Resource("video", null, null),
            new Resource("image", null, null)
    );
    Resource resouce = resources.get(0);

    @Test
    public void findResources() throws Exception {
        when(resourceService.findResources()).thenReturn(resources);
        mvc.perform(get("/resources")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "  {\"name\": \"audio\"}," +
                        "  {\"name\": \"video\"}," +
                        "  {\"name\": \"image\"}" +
                        "]"));
        verify(resourceService, times(1)).findResources();
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void findResource() throws Exception {
        when(resourceService.findResource(1L))
                .thenReturn(resouce);
        mvc.perform(get("/resources/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\": \"audio\"}"));
        verify(resourceService, times(1)).findResource(1L);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    public void createResource() throws Exception {
        mvc.perform(post("/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"audio\"}"))
                .andExpect(status().isOk());
        verify(resourceService).saveResource(eq(resouce));
    }

    @Test
    public void updateResource() throws Exception {
        mvc.perform(put("/resources/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"audio\"}"))
                .andExpect(status().isOk());
        verify(resourceService).saveResource(eq(resouce));
    }

    @Test
    public void deleteResource() throws Exception {
        mvc.perform(delete("/resources/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(resourceService).deleteResource(eq(1L));
    }

}