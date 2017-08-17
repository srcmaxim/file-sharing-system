package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(value = "")
    public String shouldFindUsersView(Model model) {
        model.addAttribute("users", service.findUsers());
        return "users/findAll";
    }

    @RequestMapping(value = "/{id}")
    public String shouldFindUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findUser(id));
        return "users/findOneOrDelete";
    }

    @RequestMapping(value = "/create")
    public String shouldCreateUserView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("type", "create");
        return "users/createOrUpdate";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String shouldCreateUser(User user) {
        service.saveUser(user);
        return "redirect:/users/" + user.getId();
    }

    @RequestMapping(value = "/{id}/edit")
    public String shouldUpdateUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findUser(id));
        model.addAttribute("type", "update");
        return "users/createOrUpdate";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String shouldUpdateUser(@PathVariable Long id, User user) {
        user.setId(id);
        service.saveUser(user);
        return "redirect:/users/" + id;
    }

    @RequestMapping(value = "/{id}/delete")
    public String shouldDeleteUserView(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findUser(id));
        model.addAttribute("type", "delete");
        return "users/findOneOrDelete";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String shouldDeleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/users";
    }

}
