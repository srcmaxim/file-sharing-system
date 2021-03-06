package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.File;
import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.Resource;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.system.DbConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(DbConfig.class)
@Transactional
public class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        String password = new StandardPasswordEncoder().encode("userPassword");
        User user = User.createNewUser("userLogin", password);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("user_user-user.user@user.user");
        user.setPhone("+3(099)-123-4567");
        this.user = user;
    }

    @Test
    public void shouldFindAllImportedResources() {
        List<Resource> resources = resourceRepository.findAll();

        assertThat(resources).isNotNull().isNotEmpty().doesNotContainNull().hasSize(4);
    }

    @Test
    public void shouldFindCreatedResourcesForNewUser() {
        userRepository.save(user);

        user = userRepository.findOne(user.getId());

        assertThat(user).isNotNull();
        assertThat(user.getResources()).hasSize(3);
        Object[] resourceNames = user.getResources().stream()
                .map(Resource::getName).toArray();
        assertThat(resourceNames).containsAll(asList("audio", "video", "image"));
    }

    @Test
    public void shouldCreatedAudioVideoImageResourcesForNewUser() {
        user = userRepository.save(user);
        user = userRepository.findOne(user.getId());

        assertThat(user.getResources()).hasSize(3);
        assertThat(user.getResources(), contains(
                hasProperty("name", is("audio")),
                hasProperty("name", is("video")),
                hasProperty("name", is("image"))
        ));
    }

    @Test
    public void shouldUpdateCreatedResourcesForNewUser() {
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