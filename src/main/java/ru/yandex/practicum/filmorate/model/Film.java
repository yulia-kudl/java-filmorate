package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.DateAfter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonIgnore
    @Builder.Default
    Set<Integer> likes = new HashSet<>();
    @Builder.Default
    Set<Genre> genres = new HashSet<>();
    Mpa mpa;

    public void setLikes(Set<Integer> likes) {
        this.likes = (likes == null) ? new HashSet<>() : likes;
    }

    @JsonProperty("genres")
    public List<Genre> getGenresSorted() {
        if (genres == null) return List.of();
        return genres.stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .toList();
    }

}
