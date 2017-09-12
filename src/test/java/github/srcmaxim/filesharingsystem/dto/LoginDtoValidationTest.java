package github.srcmaxim.filesharingsystem.dto;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class LoginDtoValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateLoginDto() {
        LoginDto user = new LoginDto("User95", "Very11Good11Pass");

        Set<ConstraintViolation<LoginDto>> validate = validator.validate(user);

        assertThat(validate).hasSize(0);
    }

    @Test
    public void shouldNotValidateLoginDtoIfFieldsAreEmpty() {
        LoginDto user = new LoginDto("", "");

        Set<ConstraintViolation<LoginDto>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(2));
    }

    @Test
    public void shouldNotValidateLoginDtoIfFieldsAreNull() {
        LoginDto user = new LoginDto();

        Set<ConstraintViolation<LoginDto>> validate = validator.validate(user);

        MatcherAssert.assertThat(validate.size(), greaterThanOrEqualTo(2));
    }

}