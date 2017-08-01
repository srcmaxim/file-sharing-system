package github.srcmaxim.filesharingsystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Role {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Long id;
    private String name;

}
