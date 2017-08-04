package github.srcmaxim.filesharingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "parent","users"})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Folder parent;

    @Setter(AccessLevel.NONE)
    @JsonIgnoreProperties("resources")
    @ManyToMany(fetch = FetchType.EAGER)
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
        while (parent != null) {
            sb.insert(0, parent.getName() + "/");
        }
        return sb.toString();
    }

}
