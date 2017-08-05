package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService service;

    @RequestMapping("")
    @ResponseBody
    public List<Resource> findResources() {
        return service.findResources();
    }

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public Resource findResource(@PathVariable Long id) {
        return service.findResource(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Resource createResource(@RequestBody Resource resource) {
        return service.saveResource(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Resource updateResource(@PathVariable Long id, @RequestBody  Resource resource) {
        resource.setId(id);
        return service.saveResource(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Resource deleteResource(@PathVariable Long id) {
        return service.deleteResource(id);
    }

}
