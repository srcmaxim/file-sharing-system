package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Loggable
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private ResourceService resourceService;

    @Autowired
    public UserController(UserService service, ResourceService resourceService) {
        this.userService = service;
        this.resourceService = resourceService;
    }

    @RequestMapping(value = "")
    public String findUsersView(Model model) {
        model.addAttribute("users", userService.findUsers());
        return "users/findAll";
    }

    @RequestMapping(value = "/{id}")
    public String findUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUser(id));
        return "users/findOneOrDelete";
    }

    @RequestMapping(value = "/create")
    public String createUserView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("type", "create");
        return "users/createOrUpdate";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("type", "create");
            return "users/createOrUpdate";
        }
        userService.saveUser(user);
        return "redirect:/users/" + user.getId();
    }

    @RequestMapping(value = "/{id}/edit")
    public String updateUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUser(id));
        model.addAttribute("type", "update");
        return "users/createOrUpdate";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("type", "update");
            return "users/createOrUpdate";
        }
        user = userService.updateUser(user);
        return "redirect:/users/" + user.getId();
    }

    @RequestMapping(value = "/{id}/delete")
    public String deleteUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUser(id));
        model.addAttribute("type", "delete");
        return "users/findOneOrDelete";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/{id}/resources", method = RequestMethod.POST)
    public String addUserToResource(@PathVariable Long id, Long resourceId) {
        resourceService.addUserToResource(resourceId, id);
        return "redirect:/users/" + id;
    }

    @RequestMapping(value = "/{id}/resources/{resourceId}/delete", method = RequestMethod.POST)
    public String removeUserFromResource(@PathVariable Long id, @PathVariable Long resourceId) {
        resourceService.removeUserFromResource(resourceId, id);
        return "redirect:/users/" + id;
    }

}
