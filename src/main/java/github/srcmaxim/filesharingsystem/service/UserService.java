package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.GenericUser;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.model.VerificationToken;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.repository.VerificationTokenRepository;
import github.srcmaxim.filesharingsystem.util.PasswordGenerator;
import org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator;
import org.slf4j.Logger;
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

    private UserRepository userRepository;
    private VerificationTokenRepository tokenRepository;
    private EmailService emailService;
    private PasswordEncoder encoder;

    @Loggable
    private Logger logger;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository,
                       EmailService emailService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.encoder = encoder;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUser(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User deleteUser(Long id) {
        User user = findUser(id);
        if (user != null) {
            userRepository.delete(id);
        }
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User updateUser(User user) {
        User oldUser = userRepository.findOne(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setPassword(user.getPassword());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        userRepository.save(oldUser);
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
        user = userRepository.save(user);
        VerificationToken token = new VerificationToken(user);
        tokenRepository.save(token);
        try {
            emailService.sendVerificationToken(token);
        } catch (MessagingException e) {
            throw new ServiceException("mail-service-not-working");
        }
        return user;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public void principalsExist(GenericUser user, BindingResult result) {
        if (userRepository.existsByLogin(user.getLogin())) {
            result.addError(new FieldError(result.getObjectName(), "login", "error.user.login.non-unique"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            result.addError(new FieldError(result.getObjectName(), "email", "error.user.email.non-unique"));
        }
        if (userRepository.existsByPhone(user.getPhone())) {
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

    @Transactional(Transactional.TxType.REQUIRED)
    public void completeRegistration(String token) {
        VerificationToken verificationToken = findVerificationToken(token);
        deleteVerificationToken(verificationToken);
        if (verificationToken == null) {
            throw new ServiceException("no-such-token");
        }
        if (verificationToken.isExpired()) {
            throw new ServiceException("expired-token");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        saveUser(user);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public VerificationToken findVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deleteVerificationToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

}
