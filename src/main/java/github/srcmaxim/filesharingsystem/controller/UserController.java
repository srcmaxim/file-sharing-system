package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("")
    @ResponseBody
    public List<User> findUsers() {
        return service.findUsers();
    }

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public User findUser(@PathVariable Long id) {
        return service.findUser(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user){
        return service.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return service.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public User deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

}
