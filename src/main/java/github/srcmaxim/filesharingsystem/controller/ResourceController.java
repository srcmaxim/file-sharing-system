package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Loggable
@RequestMapping("/resources")
public class ResourceController {

    @Loggable
    private Logger logger;

    private ResourceService service;

    @Autowired
    public ResourceController(ResourceService service) {
        this.service = service;
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
        model.addAttribute("resource", new File());
        model.addAttribute("type", "create");
        return "resources/createOrUpdate";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createResource(@Valid Resource resource, BindingResult result,
                                 Long parentId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("resource", resource);
            model.addAttribute("type", "create");
            return "resources/createOrUpdate";
        }
        resource = service.saveResource(resource, parentId);
        return "redirect:/resources/" + resource.getId();
    }

    @RequestMapping(value = "/{id}/edit")
    public String updateResourceView(@PathVariable Long id, Model model) {
        Resource resource = service.findResource(id);
        model.addAttribute("resource", resource);
        model.addAttribute("type", "update");
        return "resources/createOrUpdate";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateResource(@Valid Resource resource, BindingResult result,
                                 Long parentId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("resource", resource);
            model.addAttribute("type", "update");
            return "resources/createOrUpdate";
        }
        resource = service.updateResource(resource, parentId);
        return "redirect:/resources/" + resource.getId();
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

    @RequestMapping(value = "/{id}/users", method = RequestMethod.POST)
    public String addUserToResource(@PathVariable Long id, Long userId) {
        Resource resource = service.addUserToResource(id, userId);
        return "redirect:/resources/" + resource.getId();
    }

    @RequestMapping(value = "/{id}/users/{userId}/delete", method = RequestMethod.POST)
    public String removeUserFromResource(@PathVariable Long id, @PathVariable Long userId) {
        Resource resource = service.removeUserFromResource(id, userId);
        return "redirect:/resources/" + resource.getId();
    }

}
