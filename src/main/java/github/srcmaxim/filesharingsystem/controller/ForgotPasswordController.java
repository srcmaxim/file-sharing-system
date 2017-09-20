package github.srcmaxim.filesharingsystem.controller;


import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.service.ServiceException;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Loggable
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private UserService userService;

    @Autowired
    public ForgotPasswordController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    public String forgotPasswordView() {
        return "forgot-password";
    }

    @RequestMapping(params = "email")
    public String forgotPassword(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        try {
            userService.changePassword(email);
        } catch (ServiceException e) {
            model.addAttribute("send", false);
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
        model.addAttribute("send", true);
        return "forgot-password";
    }

}
