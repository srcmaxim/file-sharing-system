package github.srcmaxim.filesharingsystem.system;

import github.srcmaxim.filesharingsystem.mapper.ResourceValidAnnotationMethodProcessor;
import github.srcmaxim.filesharingsystem.mapper.UserValidAnnotationMethodProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

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
        argumentResolvers.add(new ResourceValidAnnotationMethodProcessor());
        argumentResolvers.add(new UserValidAnnotationMethodProcessor(encoder));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }

}
