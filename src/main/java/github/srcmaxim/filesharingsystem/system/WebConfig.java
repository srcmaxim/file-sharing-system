package github.srcmaxim.filesharingsystem.system;

import github.srcmaxim.filesharingsystem.controller.mapper.ResourceMethodArgumentResolver;
import github.srcmaxim.filesharingsystem.controller.mapper.UserMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Component
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ResourceMethodArgumentResolver());
        argumentResolvers.add(new UserMethodArgumentResolver());
    }

}
