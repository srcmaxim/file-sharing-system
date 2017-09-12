package github.srcmaxim.filesharingsystem.dto;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class RegistrationDtoTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateRegistrationDto() {
        RegistrationDto user = new RegistrationDto(
                "login",
                "password", "password",
                "Firstname", "Lastname",
                "email123_email-email.email@email123.email.email",
                "+1(111)-111-1111");

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        assertThat(validate).hasSize(0);
    }

    @Test
    public void shouldNotValidateRegistrationDtoIfFieldsAreEmpty() {
        RegistrationDto user = new RegistrationDto(
                "",
                "", "",
                "", "",
                "",""
                );

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(6));
    }
    @Test
    public void shouldNotValidateRegistrationDtoIfFieldsAreNull() {
        RegistrationDto user = new RegistrationDto(
                null,
                null,null,
                null,null,
                null,null);

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(6));
    }

    @Test
    public void shouldNotValidateRegistrationDtoWithWrongEmail() {
        RegistrationDto user = new RegistrationDto(
                "login",
                "password", "password",
                "Firstname", "Lastname",
                "email_email/-email.email@email.email.email123",
                "+1(111)-111-1111");

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        assertThat(validate).hasSize(1);
    }

    @Test
    public void shouldNotValidateRegistrationDtoWithWrongPhone() {
        RegistrationDto user = new RegistrationDto(
                "login",
                "password", "password",
                "Firstname", "Lastname",
                "email_email-email.email@email.email.email",
                "+a(111)-111-1111");

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        assertThat(validate).hasSize(1);
    }

    @Test
    public void shouldNotValidateRegistrationDtoIfPasswordAndPasswordConfirmationNotEqual() {
        RegistrationDto user = new RegistrationDto(
                "login",
                "password", "differentPassword",
                "Firstname", "Lastname",
                "email_email-email.email@email.email.email",
                "+1(111)-111-1111");

        Set<ConstraintViolation<RegistrationDto>> validate = validator.validate(user);

        assertThat(validate).hasSize(1);
    }

}