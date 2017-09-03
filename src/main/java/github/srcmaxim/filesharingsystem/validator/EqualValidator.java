package github.srcmaxim.filesharingsystem.validator;

import github.srcmaxim.filesharingsystem.annotation.Equals;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;


public class EqualValidator implements ConstraintValidator<Equals, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(Equals annotation) {
        firstFieldName = annotation.firstField();
        secondFieldName = annotation.secondField();
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object validated, ConstraintValidatorContext context) {
        Class<?> clazz = validated.getClass();
        String first = getFieldValue(clazz, validated, firstFieldName);
        String second = getFieldValue(clazz, validated, secondFieldName);
        boolean equal = first != null && first.equals(second);
        if (!equal) {
            addMessage(context, firstFieldName, message);
        }
        return equal;
    }

    private String getFieldValue(Class<?> clazz, Object validated, String firstFieldName) {
        Field field = ReflectionUtils.findField(clazz, firstFieldName);
        field.setAccessible(true);
        return (String) ReflectionUtils.getField(field, validated);
    }

    private void addMessage(ConstraintValidatorContext context, String fieldName, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(fieldName).addConstraintViolation();
    }

}
