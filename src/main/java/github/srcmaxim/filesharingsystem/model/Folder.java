package github.srcmaxim.filesharingsystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@DiscriminatorValue("folder")
public class Folder extends Resource {

    @OneToMany(targetEntity = Resource.class,
            mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();

    public Folder(String name, Folder parent, User user) {
        super(name, parent, user);
        this.resources = resources;
    }

    @Override
    public boolean isFile() {
        return false;
    }

}
