package github.srcmaxim.filesharingsystem.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("file")
public class File extends Resource {

    public File(String name, Folder parent, List<User> users) {
        this(null, name, parent, users);
    }

    public File(Long id, String name, Folder parent, List<User> users) {
        super(id, name, parent, users);
    }

    @Override
    @Pattern(regexp = "\\p{Alnum}+(\\.\\p{Alnum}+|\\_\\p{Alnum}+|\\-\\p{Alnum}+)*\\.\\p{Alnum}+",
            message = "error.resource.name.non-valid")
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean isFile() {
        return true;
    }

}

