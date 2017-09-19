package github.srcmaxim.filesharingsystem.controller;


import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

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
        model.addAttribute("send", true);
        try {
            userService.changePassword(email);
        } catch (MessagingException e) {
            model.addAttribute("error", "forgot.no-such-email");
        }
        model.addAttribute("email", email);
        return "forgot-password";
    }

}
