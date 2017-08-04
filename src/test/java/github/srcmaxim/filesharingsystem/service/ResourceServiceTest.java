package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.ResourceRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceServiceTest {

    private ResourceService service = new ResourceService();
    private ResourceRepository repository;
    private User user = User.createNewUser("jack", "p1");
    private List<Resource> resourceList = user.getResources();

    @Before
    public void init() throws Exception {
        repository = mock(ResourceRepository.class);
        service.setRepository(repository);
    }

    @Test
    public void shouldFindResources() throws Exception {
        when(repository.findAll()).thenReturn(resourceList);

        List<Resource> resources = service.findResources();

        assertThat(resources).contains(resourceList.toArray(new Resource[0]));
    }

    @Test
    public void shouldFindSavedResource() throws Exception {
        Resource resource = mock(Resource.class);
        when(repository.findOne(1L)).thenReturn(resource);
        when(resource.getId()).thenReturn(1L).thenReturn(1L);

        service.saveResource(resource);
        Resource savedResource = service.findResource(1L);

        assertThat(savedResource.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        Resource resource = mock(Resource.class);
        when(repository.save(resource)).thenReturn(resource);
        when(repository.findOne(1L)).thenReturn(resource).thenReturn(resource).thenReturn(null);
        when(resource.getId()).thenReturn(1L).thenReturn(1L);

        service.saveResource(resource);
        Resource savedResource = service.findResource(1L);
        Resource deletedResource = service.deleteResource(savedResource.getId());
        Resource findedResource = service.findResource(1L);

        assertThat(savedResource.getId()).isEqualTo(1L);
        assertThat(deletedResource.getId()).isEqualTo(1L);
        assertThat(findedResource).isNull();
    }

}