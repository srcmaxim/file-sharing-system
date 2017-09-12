package github.srcmaxim.filesharingsystem.model;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class UserValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateUser() {
        User user = new User(null,
                "login", "password",
                "Firstname", "Lastname",
                "email123_email-email.email@email123.email.email","+1(111)-111-1111",
                null, Collections.emptyList());
        Set<ConstraintViolation<User>> validate = validator.validate(user);

        assertThat(validate).hasSize(0);
    }

    @Test
    public void shouldNotValidateUserIfFieldsAreEmpty() {
        User user = new User(null,
                "", "",
                "", "",
                "","",
                null, Collections.emptyList());
        Set<ConstraintViolation<User>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(6));
    }
    @Test
    public void shouldNotValidateUserIfFieldsAreNull() {
        User user = new User(null,
                null,null,
                null,null,
                null,null,
                null, Collections.emptyList());
        Set<ConstraintViolation<User>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(6));
    }

    @Test
    public void shouldNotValidateUserWithWrongEmail() {
        User user = new User(null,
                "login", "password",
                "Firstname", "Lastname",
                "email_email/-email.email@email.email.email123","+1(111)-111-1111",
                null, Collections.emptyList());
        Set<ConstraintViolation<User>> validate = validator.validate(user);

        assertThat(validate).hasSize(1);
    }

    @Test
    public void shouldNotValidateUserWithWrongPhone() {
        User user = new User(null,
                "login", "password",
                "Firstname", "Lastname",
                "email_email-email.email@email.email.email","+a(111)-111-1111",
                null, Collections.emptyList());
        Set<ConstraintViolation<User>> validate = validator.validate(user);

        assertThat(validate).hasSize(1);
    }

}
