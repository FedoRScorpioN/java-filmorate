package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@Valid
public class User {
    private int id;
    @Email(message = "Невалидный E-mail.")
    private String email;
    @NotNull
    @NotBlank(message = "Логин не может быть пустым.")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private List<Integer> friends;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}