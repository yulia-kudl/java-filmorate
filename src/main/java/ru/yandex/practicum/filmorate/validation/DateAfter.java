package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateAfterValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAfter {

    String message() default "Дата должна быть позже {date}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String date();
}
