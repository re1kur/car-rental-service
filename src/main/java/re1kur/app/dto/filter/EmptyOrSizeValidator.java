package re1kur.app.dto.filter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import re1kur.app.util.annotations.EmptyOrSize;

public class EmptyOrSizeValidator implements ConstraintValidator<EmptyOrSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(EmptyOrSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty())
            return true;
        return value.length() >= min && value.length() <= max;
    }

}
