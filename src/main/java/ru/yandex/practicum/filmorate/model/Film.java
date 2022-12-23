package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 10, max = 200, message = "Описание фильма не может быть более 200 знаков.")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть больше 1.")
    private Integer duration;
    private int rate;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private List<Integer> userLikes = new ArrayList<>();
}