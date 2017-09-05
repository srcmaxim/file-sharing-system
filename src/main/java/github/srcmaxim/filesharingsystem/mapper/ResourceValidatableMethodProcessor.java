package github.srcmaxim.filesharingsystem.mapper;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

public class ResourceValidatableMethodProcessor extends ValidatableMethodProcessor {

    @Override
    public Class<?> supportsType() {
        return Resource.class;
    }

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