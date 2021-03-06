package github.srcmaxim.filesharingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"parent", "users"})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "error.resource.name.non-null")
    @Size(min = 1, max = 32, message = "error.resource.name.size")
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Folder parent;

    @Setter(AccessLevel.NONE)
    @JsonIgnoreProperties("resources")
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<User> users = new ArrayList<>();

    public Resource(String name, Folder parent, User user) {
        this.name = name;
        this.parent = parent;
        getUsers().add(user);
    }

    public boolean isFile() {
        return false;
    }

    public String getFullPath() {
        StringBuilder sb = new StringBuilder(name);
        Folder parent  = getParent();
        while (parent != null) {
            sb.insert(0, parent.getName() + "/");
            parent = parent.getParent();
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;
        return name != null ? name.equals(resource.name) : resource.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getType() {
        return getClass().getSimpleName().toLowerCase();
    }

}
