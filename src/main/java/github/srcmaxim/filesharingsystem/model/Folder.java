package github.srcmaxim.filesharingsystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@DiscriminatorValue("folder")
public class Folder extends Resource {

    @OneToMany(targetEntity = Resource.class,
            mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Resource> resources;

    public Folder(Long id, String name, Folder parent, List<User> users, List<Resource> resources) {
        super(id, name, parent, users);
        this.resources = resources;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    public static List<Folder> createFoldersForNewUser(User user) {
        List<User> users = asList(user);
        List<Resource> resources = emptyList();
        return asList(
                new Folder(null, "audio", null, users, resources),
                new Folder(null, "video", null, users, resources),
                new Folder(null, "image", null, users, resources));
    }

}
