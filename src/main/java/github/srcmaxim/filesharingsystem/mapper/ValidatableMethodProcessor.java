package github.srcmaxim.filesharingsystem.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * A Servlet-specific {@link ModelAttributeMethodProcessor} that applies data
 * binding through a WebDataBinder of type {@link ServletRequestDataBinder}.
 * Is a specific implementation of {@Link ServletModelAttributeMethodProcessor}.
 * Main difference from {@Link ServletModelAttributeMethodProcessor} --
 * require own implementation of {@code createAttribute()} method.
 * <p>
 * <p>Also adds an {@code getIdAttribute()} realization for implementation classes.
 */
public abstract class ValidatableMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

    /**
     * Return {@code true} if there is a method-level in default resolution mode,
     * for any return value type that is not a simple type.
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return !BeanUtils.isSimpleProperty(returnType.getParameterType());
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
     * Add non-null return values to the {@link ModelAndViewContainer}.
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        if (returnValue != null) {
            String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
            mavContainer.addAttribute(name, returnValue);
        }
    }

    /**
     * Resolve the argument from the model and validated.
     */
    @Override
    public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = ModelFactory.getNameForParameter(parameter);
        Object attribute = createAttribute(name, parameter, binderFactory, webRequest);

        WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
        if (binder.getTarget() != null) {
            validate(binder, parameter);
        }

        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
    }

    /**
     * @return attribute created form request.
     */
    protected abstract Object createAttribute(String attributeName, MethodParameter methodParam, WebDataBinderFactory binderFactory, NativeWebRequest request);

    /**
     * Validate the model attribute if applicable.
     * <p>The default implementation checks for {@code @javax.validation.Valid},
     * Spring's {@link org.springframework.validation.annotation.Validated},
     * and custom annotations whose name starts with "Valid".
     *
     * @param binder      the DataBinder to be used
     * @param methodParam the method parameter
     */
    protected void validate(WebDataBinder binder, MethodParameter methodParam) {
        Annotation ann = methodParam.getParameterAnnotation(Valid.class);
        Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
        if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
            Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
            Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            binder.validate(validationHints);
        }
    }

    /**
     * Returns id parameter/attribute of given Attribute.
     *
     * @return id parameter/attribute is such order:
     * 1. idAtribute -- if can be parsed
     * 2. idParameter -- if can be parsed
     * 3. null
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

}
