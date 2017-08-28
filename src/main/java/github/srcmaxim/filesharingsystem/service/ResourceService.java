package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.ResourceRepository;
import github.srcmaxim.filesharingsystem.system.log.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Loggable
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

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource saveResource(Resource resource, Long parentId) {
        resource = saveResource(resource);
        if (parentId != null && parentId != 0) {
            addParentToResource(resource, parentId);
        } else {
            removeParentFromResource(resource);
        }
        return resource;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource updateResource(Resource resource, Long parentId) {
        resource = updateResource(resource);
        if (parentId != null && parentId != 0) {
            addParentToResource(resource, parentId);
        } else {
            removeParentFromResource(resource);
        }
        return resource;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource addParentToResource(Resource resource, Long parentId) {
        Folder parent = (Folder) repository.findOne(parentId);
        resource.setParent(parent);
        return repository.save(resource);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource removeParentFromResource(Resource resource) {
        resource.setParent(null);
        return repository.save(resource);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource addUserToResource(Long id, Long userId) {
        Resource resource = repository.findOne(id);
        User user = userService.findUser(userId);
        resource.getUsers().add(user);
        return repository.save(resource);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Resource removeUserFromResource(Long id, Long userId) {
        Resource resource = repository.findOne(id);
        User user = userService.findUser(userId);
        resource.getUsers().remove(user);
        return repository.save(resource);
    }

}
