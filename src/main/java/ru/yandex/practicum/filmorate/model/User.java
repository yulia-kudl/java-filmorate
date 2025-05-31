package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;


@Data
@Builder
public class User {
    Integer id;
    @Email(message = "поле email не соответствует формату")
    String email;
    @NotBlank(message = "login не может быть пустым или содержать только пробелы")
    String login;
    String name;
    @Past(message = "дата рождения должна быть в прошлом")
    LocalDate birthday;
}
