package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

public interface FilmService {
    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);
    List<Film> getPopularFilms(@Positive int count);
    Collection<Film> getAll();
    Film getFilmById(long filmId);
    Film addFilm(Film film);
    Film updateFilm(long filmId, Film film);
    void deleteFilm(long filmId);


}
