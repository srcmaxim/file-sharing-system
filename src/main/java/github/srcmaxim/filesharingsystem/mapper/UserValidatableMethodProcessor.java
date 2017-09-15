package github.srcmaxim.filesharingsystem.mapper;

import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Resolves {@code User} parameters into {@code User} attribute in
 * the context of a given request.
 * Also performs validation of {@code User} for {@link BindingResult}.
 *
 * @see ValidatableMethodProcessor
 */
public class UserValidatableMethodProcessor extends ValidatableMethodProcessor {

    private PasswordEncoder encoder;

    public UserValidatableMethodProcessor(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Class<?> supportsType() {
        return User.class;
    }

    /**
     * Extension point to create the {@code User} model attribute.
     *
     * @param attributeName the name of the attribute (never {@code null})
     * @param methodParam   the method parameter
     * @param binderFactory for creating WebDataBinder instance
     * @param request       the current request
     * @return the created model attribute (never {@code null})
     */
    @Override
    protected User createAttribute(String attributeName, MethodParameter methodParam,
                                   WebDataBinderFactory binderFactory, NativeWebRequest request) {

        Long id = getIdAttribute(request);

        String login = request.getParameter("login");
        String password = encoder.encode(request.getParameter("password"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        return new User(id, login, password, firstName, lastName, email, phone, null, null);
    }

}