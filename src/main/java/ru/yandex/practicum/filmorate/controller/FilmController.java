package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        return filmService.createFilms(film);
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film film) {
        return filmService.updateFilms(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film getByIdFilms(@PathVariable String id) {
        return filmService.getByIdFilms(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeFilms(@PathVariable String filmId, @PathVariable String userId) {
        filmService.addLikeFilms(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLikeFilms(@PathVariable String filmId, @PathVariable String userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(count);
    }
}