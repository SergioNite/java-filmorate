package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static Long currentMaxId = 1L;
    private static Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid  @RequestBody Film film) {
        film.setId(currentMaxId++);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("addFilm {}",film);
        } else {
            throw new ValidationException("Wrong film ID");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("updateFilm {}",film);
        } else {
            throw new ValidationException("Wrong film ID");
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

}
