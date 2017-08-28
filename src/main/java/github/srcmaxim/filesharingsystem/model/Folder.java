package github.srcmaxim.filesharingsystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "resources", callSuper = true)
@ToString(exclude = "resources")
@Entity
@DiscriminatorValue("folder")
public class Folder extends Resource {

    @OneToMany(targetEntity = Resource.class,
            mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();

    public Folder(String name, Folder parent, List<User> users) {
        this(null, name, parent, users);
    }

    public Folder(Long id, String name, Folder parent, List<User> users) {
        super(id, name, parent, users);
    }

    @Override
    @Pattern(regexp = "\\p{Alnum}+(\\.\\p{Alnum}+|\\_\\p{Alnum}+|\\-\\p{Alnum}+)*",
            message = "error.resource.name.non-valid")
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean isFile() {
        return false;
    }

}
