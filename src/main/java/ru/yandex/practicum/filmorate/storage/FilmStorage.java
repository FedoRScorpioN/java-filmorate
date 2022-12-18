package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film createFilms(Film film);

    Film updateFilms(Film film);

    Collection<Film> findAllFilms();

    Film getByIdFilms(int id);

    Film deleteByIdFilms(int id);

    Map<Integer, Film> getFilms();
}