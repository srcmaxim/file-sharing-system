package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.dto.UserDto;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.UserPrincipalsService;
import github.srcmaxim.filesharingsystem.service.UserService;
import github.srcmaxim.filesharingsystem.system.log.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Loggable
public class SecurityController {

    private UserService userService;
    private UserPrincipalsService securityService;

    @Autowired
    public SecurityController(UserService userService, UserPrincipalsService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/register")
    public String registerView(Model model) {
        model.addAttribute("user", new User());
        return "security/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Errors errors, Model model) {
        User registered = null;
        if (!result.hasErrors()) {
            registered = userService.createUserAccount(user, result);
            if (registered != null && !result.hasErrors()) {
                model.addAttribute("user", user);
                return "redirect:/login";
            }
        }
        model.addAttribute("user", user);
        return "security/register";
    }

    @RequestMapping(value = "/login")
    public String loginView(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "security/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "security/login";
        }

        UserDetails userDetails;
        try {
            userDetails = securityService.loadUserByUsername(userDto.getLogin());
        } catch (UsernameNotFoundException e) {
            result.addError(new FieldError(result.getObjectName(), "login", e.getMessage()));
            model.addAttribute("userDto", userDto);
            return "security/login";
        }

        if (userDto.equals(userDetails.getPassword())) {
            result.addError(new FieldError(result.getObjectName(), "password", "error.user.password.not-match"));
            model.addAttribute("userDto", userDto);
            return "security/login";
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername (),userDetails.getPassword (),userDetails.getAuthorities ());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/users";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }

}
