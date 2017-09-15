package github.srcmaxim.filesharingsystem.system;

import github.srcmaxim.filesharingsystem.mapper.ResourceValidatableMethodProcessor;
import github.srcmaxim.filesharingsystem.mapper.UserValidatableMethodProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Component
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ResourceValidatableMethodProcessor());
        argumentResolvers.add(new UserValidatableMethodProcessor(encoder));
    }

}
