package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(DbConfig.class)
@Transactional
public class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserRepository userRepository;

    private static User getUser() {
        User user = User.createNewUser("userLogin", "userPassword");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("user_user-user.user@user.user");
        user.setPhone("+3(099)-123-4567");
        return user;
    }

    @Test
    public void shouldFindAllImportedResources() {
        List<Resource> resources = resourceRepository.findAll();

        assertThat(resources).isNotNull().isNotEmpty().doesNotContainNull().hasSize(4);
    }

    @Test
    public void shouldFindCreatedResourcesForNewUser() {
        User user = getUser();
        userRepository.save(user);

        user = userRepository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertThat(user.getResources()).hasSize(3);
        Object[] resourceNames = user.getResources().stream()
                .map(Resource::getName).toArray();
        assertThat(resourceNames).containsAll(asList("audio", "video", "image"));
    }

    private Folder getFolderByName(User user, String name) {
        Folder folder = null;
        for (Resource resource : user.getResources()) {
            folder = (Folder) resource;
            if (folder.getName().equals(name)) {
                folder = (Folder) resource;
            }
        }
        return folder;
    }

}