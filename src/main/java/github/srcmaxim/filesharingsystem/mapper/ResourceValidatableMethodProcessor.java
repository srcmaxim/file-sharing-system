package github.srcmaxim.filesharingsystem.mapper;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import org.springframework.core.MethodParameter;
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
public class ResourceValidatableMethodProcessor extends ValidatableMethodProcessor {

    @Override
    public Class<?> supportsType() {
        return Resource.class;
    }

    /**
     * Extension point to create the {@code Resource} model attribute.
     *
     * @param attributeName the name of the attribute (never {@code null})
     * @param methodParam   the method parameter
     * @param binderFactory for creating WebDataBinder instance
     * @param request       the current request
     * @return the created model attribute (never {@code null})
     */
    @Override
    protected Resource createAttribute(String attributeName, MethodParameter methodParam,
                WebDataBinderFactory binderFactory, NativeWebRequest request) {

        Long id = getIdAttribute(request);
        String name = request.getParameter("name");
        String type = request.getParameter("type");

        if ("file".equals(type)) {
            return new File(id, name, null, null);
        } else if ("folder".equals(type)) {
            return new Folder(id, name, null, null);
        }
        throw new IllegalArgumentException("Type must be file or folder in " + getClass().getSimpleName());
    }

}