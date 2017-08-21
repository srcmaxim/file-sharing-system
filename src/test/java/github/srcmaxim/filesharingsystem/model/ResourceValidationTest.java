package github.srcmaxim.filesharingsystem.model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceValidationTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateFolder() {
        Folder parent = new Folder("folder", null, Collections.emptyList());
        Folder folder = new Folder("folDER-Folder_FOLDER.folder", parent, Collections.emptyList());
        Set<ConstraintViolation<Folder>> validate = validator.validate(folder);

        assertThat(validate).hasSize(0);
    }

    @Test
    public void shouldNotValidateFolderWithWrongName() {
        Folder folder = new Folder("/wrong-name/", null, Collections.emptyList());
        Set<ConstraintViolation<Folder>> validate = validator.validate(folder);

        assertThat(validate).hasSize(1);
    }

    @Test
    public void shouldNotValidateFolderWithWrongSizeOfName() {
        Folder folder = new Folder("123456789012345678901234567890-wrong-part", null, Collections.emptyList());
        Set<ConstraintViolation<Folder>> validate = validator.validate(folder);

        assertThat(validate).hasSize(1);
    }

    @Test
    public void shouldNotValidateFolderWithNullName() {
        Folder folder = new Folder(null, null, Collections.emptyList());
        Set<ConstraintViolation<Folder>> validate = validator.validate(folder);

        assertThat(validate).hasSize(1);
    }

}
