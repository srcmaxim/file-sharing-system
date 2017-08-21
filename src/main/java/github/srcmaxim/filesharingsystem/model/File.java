package github.srcmaxim.filesharingsystem.model;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import java.util.List;


@NoArgsConstructor
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
    @Pattern(regexp = "\\p{Alpha}+(\\.\\p{Alpha}+|\\_\\p{Alpha}+|\\-\\p{Alpha}+)*\\.\\p{Alpha}",
            message = "error.resource.name.non-valid")
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean isFile() {
        return true;
    }

}
