package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.Authority;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.system.log.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Loggable
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findUsers() {
        return repository.findAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUser(Long id) {
        return repository.findOne(id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User deleteUser(Long id) {
        User user = findUser(id);
        if (user != null) {
            repository.delete(id);
        }
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
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

    @Transactional(Transactional.TxType.REQUIRED)
    public User createUserAccount(User user, BindingResult result) {
        principalsExist(user, result);
        if (result.hasErrors()) {
            return null;
        }
        user.getAuthorities().add(new Authority(Authority.ROLE_USER));
        return repository.save(user);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public void principalsExist(User user, BindingResult result) {
        if (repository.existsByLogin(user.getLogin())) {
            result.addError(new FieldError(result.getObjectName(), "login","error.user.login.non-unique"));
        }
        if (repository.existsByEmail(user.getEmail())) {
            result.addError(new FieldError(result.getObjectName(), "email","error.user.email.non-unique"));
        }
        if (repository.existsByPhone(user.getPhone())) {
            result.addError(new FieldError(result.getObjectName(), "phone","error.user.phone.non-unique"));
        }
    }

}
