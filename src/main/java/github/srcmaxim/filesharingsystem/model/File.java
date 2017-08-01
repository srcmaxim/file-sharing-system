package github.srcmaxim.filesharingsystem.model;

import lombok.Data;

@Data
public class File extends Resource {
    @Override
    public boolean isFile() {
        return true;
    }
}
