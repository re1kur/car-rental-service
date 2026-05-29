package re1kur.app.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import re1kur.app.dto.filter.EmptyOrSizeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmptyOrSizeValidator.class)
public @interface EmptyOrSize {
    String message() default "Must be empty or have length between {min} and {max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();

    int max();
}
