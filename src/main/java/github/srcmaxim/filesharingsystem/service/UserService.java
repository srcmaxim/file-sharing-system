package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    @Setter
    private UserRepository repository;

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
}
