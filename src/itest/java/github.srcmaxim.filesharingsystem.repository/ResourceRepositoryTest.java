package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.system.Config;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Config.class)
@Transactional
public class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserRepository userRepository;

    private String userLogin = "userLogin";
    private String userPassword = "userPassword";

    @Test
    public void shouldFindAllImportedResources() {
        List<Resource> resources = resourceRepository.findAll();

        assertThat(resources).isNotNull().isNotEmpty().doesNotContainNull().hasSize(3);
    }

    @Test
    public void shouldFindCreatedResourcesForNewUser() {
        User user = User.createNewUser(userLogin, userPassword);
        userRepository.save(user);

        user = userRepository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertThat(user.getResources()).hasSize(3);
        Object[] resourceNames = user.getResources().stream()
                .map(Resource::getName).toArray();
        assertThat(resourceNames).containsAll(asList("audio", "video", "image"));
    }

    @Test
    public void shouldUpdateCreatedResourcesForNewUser() {
        User user = User.createNewUser(userLogin, userPassword);
        user = userRepository.save(user);
        Folder audioFolder = getFolderByName(user, "audio");
        String songName = "Beatles - It's been a hard day night";
        audioFolder.getResources().add(
                new File(songName, audioFolder, asList(user)));

        userRepository.save(user);
        user = userRepository.findOne(user.getId());

        audioFolder = getFolderByName(user, "audio");
        File audio = (File) audioFolder.getResources().get(0);
        assertEquals(audio.getName(), songName);
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