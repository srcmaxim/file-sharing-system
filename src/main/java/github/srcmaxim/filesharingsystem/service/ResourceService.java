package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ResourceService {

    private ResourceRepository repository;
    private UserService userService;

    @Autowired
    public ResourceService(ResourceRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Resource> findResources() {
        return repository.findAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Resource findResource(Long id) {
        return repository.findOne(id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource saveResource(Resource resource) {
        return repository.save(resource);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource deleteResource(Long id) {
        Resource resource = findResource(id);
        if (resource != null) {
            repository.delete(id);
        }
        return resource;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource updateResource(Resource resource) {
        Resource oldResource = repository.findOne(resource.getId());
        oldResource.setName(resource.getName());
        repository.save(oldResource);
        resource.setId(oldResource.getId());
        return resource;
    }
}
