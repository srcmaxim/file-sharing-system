package github.srcmaxim.filesharingsystem.controller.mapper;

import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UserMethodArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> attributes = (Map<String, String>) nativeRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String id = webRequest.getParameter("id");
        id = (attributes.get("id") != null) ? attributes.get("id") : id;
        Long idNumber = ("".equals(id)) ? null : Long.valueOf(id);

        String login = webRequest.getParameter("login");
        String password = webRequest.getParameter("password");
        String firstName = webRequest.getParameter("firstName");
        String lastName = webRequest.getParameter("lastName");
        String email = webRequest.getParameter("email");
        String phone = webRequest.getParameter("phone");

        return new User(idNumber, login, password, firstName, lastName, email, phone, null, null);
    }

}