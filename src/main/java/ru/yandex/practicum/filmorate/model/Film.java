package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.validation.DateAfter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@Getter
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
    @Builder.Default
    Set<Integer> likes = new HashSet<>();

    public void setLikes(Set<Integer> likes) {
        this.likes = (likes == null) ? new HashSet<>() : likes;
    }

}
