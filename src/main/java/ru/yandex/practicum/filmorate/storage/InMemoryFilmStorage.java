package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long currentMaxId = 1L;
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(currentMaxId++);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return films.get(film.getId());
        } else throw new ValidationException(String.format("Невозможно создать фильм с id %s", film.getId()));
    }

    @Override
    public Optional<Film> updateFilm(long filmId, Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(film);
        } else {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,String.format("Невозможно обновить фильм с id %s",film.getId()));
        }
    }

    @Override
    public void deleteFilm(long filmId) {
        Optional<Film> film = getFilmById(filmId);
        if (!film.isEmpty() && !films.containsKey(filmId)){
            films.remove(filmId);
        }
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream().sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Film addLike(User user, Film film) {
        return null;
    }

    @Override
    public Film deleteLike(User user, Film film) {
        return null;
    }
}
