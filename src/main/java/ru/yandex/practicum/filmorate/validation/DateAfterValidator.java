package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private LocalDate date;

    @Override
    public void initialize(DateAfter annotation) {
        try {
            date = LocalDate.parse(annotation.date());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты в аннотации @DateAfter. Используйте yyyy-MM-dd");
        }
    }



    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true;
        log.info("началась валидация даты {}", value.toString());

        return value.isAfter(date);
    }
}
