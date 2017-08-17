package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.ResourceRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ResourceService {

    @Autowired
    @Setter
    private ResourceRepository repository;

    @Autowired
    @Setter
    private UserService userService;

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
    public Resource saveResource(Resource resource, Long parentId, List<Long> userIds, String type) {
        Folder parent = (parentId == null) ? null : (Folder) findResource(parentId);
        userIds = userIds.stream().filter(userId -> userId != null).collect(toList());
        List<User> users = userService.findUsers(userIds);
        resource = "file".equals(type)
                ? new File(resource.getName(), parent, users)
                : new Folder(resource.getName(), parent, users);
        saveResource(resource);
        return resource;
    }

}
