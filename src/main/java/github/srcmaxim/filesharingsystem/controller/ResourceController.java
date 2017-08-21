package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/resources")
public class ResourceController {

    private ResourceService service;
    private Validator validator;

    @Autowired
    public ResourceController(ResourceService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @RequestMapping("")
    public String findResourcesView(Model model) {
        model.addAttribute("resources", service.findResources());
        return "resources/findAll";
    }

    @RequestMapping(value = "/{id}")
    public String findResourceView(@PathVariable Long id, Model model) {
        model.addAttribute("resource", service.findResource(id));
        return "resources/findOneOrDelete";
    }

    @RequestMapping(value = "/create")
    public String createResourceView(Model model) {
        model.addAttribute("resource", new Resource());
        model.addAttribute("type", "create");
        model.addAttribute("resourceUserIds", "");
        return "resources/createOrUpdate";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createResource(Resource resource, Long parentId, String userIds, String type) {
        List<Long> ids = Arrays.stream(userIds.split(", ")).map(Long::valueOf).collect(toList());
        resource = service.saveResource(resource, parentId, ids, type);
        return "redirect:/resources/" + resource.getId();
    }

    @RequestMapping(value = "/{id}/edit")
    public String updateResourceView(@PathVariable Long id, Model model) {
        Resource resource = service.findResource(id);
        model.addAttribute("resource", resource);
        model.addAttribute("type", "update");
        model.addAttribute("resourceUserIds", resource.getUsers()
                .stream().map(u -> u.getId().toString())
                        .reduce((a, b) -> a + ", " + b).get());
        return "resources/createOrUpdate";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateResource(@PathVariable Long id, Resource resource, Long parentId, String userIds, String type) {
        resource.setId(id);
        List<Long> ids = Arrays.stream(userIds.split(", ")).map(Long::valueOf).collect(toList());
        resource = service.saveResource(resource, parentId, ids, type);
        return "redirect:/resources/" + id;
    }

    @RequestMapping(value = "/{id}/delete")
    public String deleteResourceView(@PathVariable Long id, Model model) {
        model.addAttribute("resource", service.findResource(id));
        model.addAttribute("type", "delete");
        return "resources/findOneOrDelete";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteResource(@PathVariable Long id) {
        service.deleteResource(id);
        return "redirect:/resources";
    }

}
