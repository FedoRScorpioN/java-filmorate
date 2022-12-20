package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilms(Film film);

    Film updateFilms(Film film);

    Collection<Film> findAllFilms();

    Film getByIdFilms(int id);

    List<Film> getFilms();

    boolean deleteFilm(Film film);

    boolean addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(Integer count);
}