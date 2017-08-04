package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository repository;

    public List<Resource> findResources() {
        return repository.findAll();
    }


    public Resource saveResource(Resource resource) {
        return repository.save(resource);
    }

    public Resource findResource(Long id) {
        return repository.getOne(id);
    }

    public Resource deleteResource(Long id) {
        Resource resource = findResource(id);
        if (resource != null) {
            repository.delete(id);
        }
        return resource;
    }
}
