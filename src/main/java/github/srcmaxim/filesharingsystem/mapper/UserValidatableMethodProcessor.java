package github.srcmaxim.filesharingsystem.mapper;

import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

public class UserValidatableMethodProcessor extends ValidatableMethodProcessor {

    @Override
    public Class<?> supportsType() {
        return User.class;
    }

    @Override
    protected User createAttribute(String attributeName, MethodParameter methodParam,
                WebDataBinderFactory binderFactory, NativeWebRequest request) {

        Long id = getIdAttribute(request);

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        return new User(id, login, password, firstName, lastName, email, phone, null, null);
    }

}