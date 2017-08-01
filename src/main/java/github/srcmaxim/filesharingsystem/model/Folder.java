package github.srcmaxim.filesharingsystem.model;

import lombok.Data;

import java.util.List;

@Data
public class Folder extends Resource {

    private List<Resource> resources;

    @Override
    public boolean isFile() {
        return false;
    }
}
