package github.srcmaxim.filesharingsystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class Resource {

    @Setter(AccessLevel.NONE)
    private Long id;
    @Setter(AccessLevel.NONE)
    private Long parentId;
    private String name;

    public abstract boolean isFile();
}
