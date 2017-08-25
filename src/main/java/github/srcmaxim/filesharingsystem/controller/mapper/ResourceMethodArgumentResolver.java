package github.srcmaxim.filesharingsystem.controller.mapper;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ResourceMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Resource.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> attributes = (Map<String, String>) nativeRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String type = webRequest.getParameter("type");
        String id = webRequest.getParameter("id");
        id = (attributes.get("id") != null) ? attributes.get("id") : id;
        String name = webRequest.getParameter("name");
        Long idNumber = ("".equals(id)) ? null : Long.valueOf(id);

        return ("file".equals(type))
                ? new File(idNumber, name, null, null)
                : new Folder(idNumber, name, null, null);
    }

}
