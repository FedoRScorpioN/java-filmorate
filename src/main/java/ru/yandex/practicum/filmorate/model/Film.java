package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Set<Integer> userLikes = new HashSet<>();
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final int duration;
}