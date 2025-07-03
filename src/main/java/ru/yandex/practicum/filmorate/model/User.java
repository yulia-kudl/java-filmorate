package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Integer id;
    @Email(message = "поле email не соответствует формату")
    String email;
    @NotBlank(message = "login не может быть пустым или содержать только пробелы")
    String login;
    String name;
    @Past(message = "дата рождения должна быть в прошлом")
    LocalDate birthday;
    @Builder.Default
    Set<Integer> friends = new HashSet<>();


    public void setFriends(Set<Integer> friends) {
        this.friends = (friends == null) ? new HashSet<>() : friends;
    }
}
