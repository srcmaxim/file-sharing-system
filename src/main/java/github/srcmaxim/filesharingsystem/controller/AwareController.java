package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Loggable
public class AwareController {

    @Loggable
    private Logger logger;

    @RequestMapping(value = "/info", params = "type")
    public String info(@RequestParam String type, Model model) {
        model.addAttribute("group", "info");
        model.addAttribute("type", type);
        return "aware";
    }

    @RequestMapping(value = "/error", params = "type")
    public String error(@RequestParam String type, Model model) {
        model.addAttribute("group", "error");
        model.addAttribute("type", type);
        return "aware";
    }

}
