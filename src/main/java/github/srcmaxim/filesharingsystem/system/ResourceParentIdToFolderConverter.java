package github.srcmaxim.filesharingsystem.system;

import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.service.ResourceService;
import org.springframework.core.convert.converter.Converter;

public class ResourceParentIdToFolderConverter implements Converter<String, Folder> {

    private ResourceService service;

    public ResourceParentIdToFolderConverter(ResourceService service) {
        this.service = service;
    }


    @Override
    public Folder convert(String parent) {
        try {
            Long resourceId = Long.valueOf(parent);
            return (Folder) service.findResource(resourceId);
        }catch (Exception e) {
            return null;
        }
    }

}
