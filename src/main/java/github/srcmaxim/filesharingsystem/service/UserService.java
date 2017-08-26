package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.system.log.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Loggable
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findUsers() {
        return repository.findAll();
    }

    public User findUser(Long id) {
        return repository.findOne(id);
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public User deleteUser(Long id) {
        User user = findUser(id);
        if (user != null) {
            repository.delete(id);
        }
        return user;
    }

    public User updateUser(User user) {
        User oldUser = repository.findOne(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setPassword(user.getPassword());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        repository.save(oldUser);
        user.setId(oldUser.getId());
        return user;
    }

}
