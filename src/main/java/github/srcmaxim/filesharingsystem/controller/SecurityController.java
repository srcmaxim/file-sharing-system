package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.dto.LoginDto;
import github.srcmaxim.filesharingsystem.dto.RegistrationDto;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.service.ServiceException;
import github.srcmaxim.filesharingsystem.service.UserPrincipalsService;
import github.srcmaxim.filesharingsystem.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@Loggable
public class SecurityController {

    private UserService userService;
    private UserPrincipalsService securityService;
    private PasswordEncoder encoder;

    @Loggable
    private Logger logger;

    @Autowired
    public SecurityController(UserService userService, UserPrincipalsService securityService,
                              PasswordEncoder encoder) {
        this.userService = userService;
        this.securityService = securityService;
        this.encoder = encoder;
    }

    @RequestMapping(value = "/register")
    public String registerView(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "security/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("registrationDto")
                           @Valid RegistrationDto registrationDto,
                           BindingResult result, Model model) {
        if (!result.hasErrors()) {
            User registered = null;
            try {
                registered = userService.createUserAccount(registrationDto, result);
            } catch (ServiceException e) {
                return "redirect:/error?type=" + e.getMessage();
            }
            if (registered != null && !result.hasErrors()) {
                return "redirect:/info?type=get-verification-token-email";
            }
        }
        model.addAttribute("registrationDto", registrationDto);
        return "security/register";
    }

    @RequestMapping(value = "/verification", params = "token")
    public String completeRegistration(@RequestParam String token, Model model) {
        try {
            userService.completeRegistration(token);
        } catch (ServiceException e) {
            return "redirect:/error?type=" + e.getMessage();
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/login")
    public String loginView(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "security/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("loginDto") @Valid LoginDto loginDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("loginDto", loginDto);
            return "security/login";
        }

        UserDetails userDetails;
        try {
            userDetails = securityService.loadUserByUsername(loginDto.getLogin());
        } catch (UsernameNotFoundException e) {
            result.addError(new FieldError(result.getObjectName(), "login", e.getMessage()));
            model.addAttribute("loginDto", loginDto);
            return "security/login";
        } catch (AccountExpiredException e) {
            return "redirect:/info?type=" + e.getMessage();
        }

        if (!encoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            result.addError(new FieldError(result.getObjectName(), "password", "error.user.password.not-match"));
            model.addAttribute("loginDto", loginDto);
            return "security/login";
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/users";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }

}
