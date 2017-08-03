package github.srcmaxim.filesharingsystem.model;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;


@NoArgsConstructor
@Entity
@DiscriminatorValue("file")
public class File extends Resource {

    public File(Long id, String name, Folder parent, List<User> users) {
        super(id, name, parent, users);
    }

    @Override
    public boolean isFile() {
        return true;
    }
}
