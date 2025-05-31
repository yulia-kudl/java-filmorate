package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.DateAfter;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    Integer id;
    @NotBlank(message = "поле name не может состоять из пробелов или быть пустым")
    String name;
    @Size(max = 200, message = "Поле descriprion не должно превышать 200 символов")
    String description;
    @DateAfter(date = "1895-12-28")
    LocalDate releaseDate;
    @Positive(message = "Поле duration должно быть положительным")
    int duration;
}
