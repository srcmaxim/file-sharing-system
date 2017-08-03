package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Folder;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        List<Folder> folders = Folder.createFoldersForNewUser(user);
        user.getResources().addAll(folders);
        return repository.save(user);
    }

    public User findUser(Long id) {
        return repository.getOne(id);
    }

    public User deleteUser(Long id) {
        User user = findUser(id);
        if (user != null) {
            repository.delete(id);
        }
        return user;
    }
}
