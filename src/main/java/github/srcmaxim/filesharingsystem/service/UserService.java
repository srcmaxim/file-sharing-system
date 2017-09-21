package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.GenericUser;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.util.PasswordGenerator;
import org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.util.List;

@Service
@Loggable
public class UserService {

    private UserRepository repository;
    private EmailService emailService;
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, EmailService emailService, PasswordEncoder encoder) {
        this.repository = repository;
        this.emailService = emailService;
        this.encoder = encoder;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findUsers() {
        return repository.findAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUser(Long id) {
        return repository.findOne(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUserByEmail(String email) {
        return repository.findByEmail(email);
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
    public User createUserAccount(GenericUser genericUser, BindingResult result) {
        principalsExist(genericUser, result);
        if (result.hasErrors()) {
            return null;
        }
        User user = createNewUser(genericUser);
        return repository.save(user);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public void principalsExist(GenericUser user, BindingResult result) {
        if (repository.existsByLogin(user.getLogin())) {
            result.addError(new FieldError(result.getObjectName(), "login", "error.user.login.non-unique"));
        }
        if (repository.existsByEmail(user.getEmail())) {
            result.addError(new FieldError(result.getObjectName(), "email", "error.user.email.non-unique"));
        }
        if (repository.existsByPhone(user.getPhone())) {
            result.addError(new FieldError(result.getObjectName(), "phone", "error.user.phone.non-unique"));
        }
    }

    private User createNewUser(GenericUser registrationDto) {
        String hashedPassword = encoder.encode(registrationDto.getPassword());
        User user = User.createNewUser(registrationDto.getLogin(), hashedPassword);

        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPhone(registrationDto.getPhone());
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void changePassword(String email) {
        validateUserEmail(email);
        User user = findUserByEmail(email);
        if (user == null) {
            throw new ServiceException("error.user.email.no-email");
        }
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useLower(true)
                .useUpper(true)
                .useDigits(true)
                .build();
        String password = passwordGenerator.generate(16);
        user.setPassword(encoder.encode(password));
        try {
            emailService.sendPasswordTo(user);
        } catch (MessagingException e) {
            throw new ServiceException("forgot.error");
        }
    }

    private static void validateUserEmail(String email) {
        PatternValidator validator = new PatternValidator();
        validator.initialize(emailPattern());
        if(!validator.isValid(email, null)) {
            throw new ServiceException("error.user.email.non-valid");
        }
    }

    private static Pattern emailPattern() {
        try {
            Field email = User.class.getDeclaredField("email");
            return email.getAnnotation(Pattern.class);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
