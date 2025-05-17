package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
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
    LocalDate releaseDate;
    @Positive(message = "Поле duration должно быть положительным")
    int duration;
}
