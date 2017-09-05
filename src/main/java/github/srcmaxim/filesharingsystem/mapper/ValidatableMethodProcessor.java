package github.srcmaxim.filesharingsystem.mapper;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * A Servlet-specific {@link ModelAttributeMethodProcessor} that applies data
 * binding through a WebDataBinder of type {@link ServletRequestDataBinder}.
 * Main difference from {@Link ServletModelAttributeMethodProcessor} --
 * require own implementation of {@code createAttribute()} method.
 *
 * <p>Also adds an {@code getIdAttribute()} realization for implementation classes.
 */
public abstract class ValidatableMethodProcessor extends ModelAttributeMethodProcessor {

    public ValidatableMethodProcessor() {
        super(false);
    }

    /**
     * Returns {@code true} if the parameter is annotated with
     * {@link Valid} and parameter type is defined in {@Link ValidatableMethodProcessor#supportsType()}
     */
    @Override
    public final boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Valid.class)
                && parameter.getParameterType().equals(supportsType());
    }

    /**
     * @return supports type for given parameter.
     */
    protected abstract Class<?> supportsType();

    /**
     * @return attribute created form request.
     */
    protected abstract Object createAttribute(String attributeName, MethodParameter methodParam, WebDataBinderFactory binderFactory, NativeWebRequest request);

    /**
     * Returns id parameter/attribute of given Attribute.
     *
     * @return      id parameter/attribute is such order:
     *              1. idAtribute -- if can be parsed
     *              2. idParameter -- if can be parsed
     *              3. null
     */
    protected final Long getIdAttribute(NativeWebRequest webRequest) {
        String idAtribute = ((Map<String, String>) webRequest.getNativeRequest(HttpServletRequest.class)
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).get("id");
        String idParameter = webRequest.getParameter("id");

        Long id = null;
        id = tryParse(idParameter, id);
        id = tryParse(idAtribute, id);
        return id;
    }

    private Long tryParse(String number, Long defaultNumber) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return defaultNumber;
        }
    }

    /**
     * The method is a strait realization from
     * {@Link ServletModelAttributeMethodProcessor#bindRequestParameters()}.
     * This implementation downcasts {@link WebDataBinder} to
     * {@link ServletRequestDataBinder} before binding.
     * Primary uses in {@Link BindingResult} parameter of the controller method.
     * @see ServletRequestDataBinderFactory
     * @see ServletModelAttributeMethodProcessor
     */
    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }

}
