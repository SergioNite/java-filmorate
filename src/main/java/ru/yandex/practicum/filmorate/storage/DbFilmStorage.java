package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.storage.mapper.MapRowToGenre;
import ru.yandex.practicum.filmorate.storage.mapper.MapRowToMpa;

import java.util.*;

@Primary
@Component
@Qualifier("DbFilmStorage")
public class DbFilmStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        int filmId = insertFilm.executeAndReturnKey(film.toMap()).intValue();

        if (film.getGenres() != null) {
            SimpleJdbcInsert insertGenre = new SimpleJdbcInsert(jdbcTemplate).withTableName("film_genres")
                    .usingColumns("film_id", "genre_id");
            film.getGenres().forEach(gen -> insertGenre.execute(Map.of("film_id", filmId,
                    "genre_id", gen.getId())));
        }
        Optional<Film> result = getFilmById(filmId);
        if (result.isEmpty())
            throw new ValidationException(String.format("Невозможно создать фильм с id %s", film.getId()));
        return result.get();
    }

    @Override
    public Optional<Film> updateFilm(long filmId, Film film) {
        try {
            String sqlQuerySearch = "SELECT film_id, film_name, film_description, " +
                    "film_release_date, film_duration, FILM_MPA_ID " +
                    "FROM films WHERE film_id = ?";
            jdbcTemplate.queryForObject(sqlQuerySearch, new BeanPropertyRowMapper<>(Film.class), filmId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        String sqlQuery = "UPDATE films SET film_name = ?, film_description = ?, film_release_date = ?, " +
                "film_duration = ?, FILM_MPA_ID = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            String sqlQueryGenresRemove = "DELETE FROM film_genres WHERE film_id = ?";
            jdbcTemplate.update(sqlQueryGenresRemove, filmId);
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film_genres")
                    .usingColumns("film_id", "genre_id");
            film.getGenres().forEach(g -> simpleJdbcInsert.execute(Map.of("film_id", filmId, "genre_id", g.getId())));
        }

        Optional<Film> result = getFilmById(filmId);
        if (film.getGenres() != null) {
            if (film.getGenres().isEmpty()) result.get().setGenres(new HashSet<>());
        }

        return result;
    }

    @Override
    public void deleteFilm(long filmId) {
        String sqlQuerySearch = "SELECT film_id, film_name, film_description, film_release_date, film_duration " +
                "FROM films WHERE film_id = ?";
        Optional<Film> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sqlQuerySearch, new BeanPropertyRowMapper<>(Film.class), filmId)
        );
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        try {
            String sqlQuery = "SELECT film_id, film_name, film_description, film_release_date, film_duration, film_mpa_id " +
                    "FROM films WHERE film_id = ?";
            Optional<Film> result = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new MapRowToFilm(), filmId));

            if (result.isPresent()) {
                Film film = result.get();

                String sqlQueryGenres = "SELECT g.genre_id, g.genre_name FROM film_genres AS fg " +
                        "JOIN genres AS g ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";
                List<Genre> genres = jdbcTemplate.query(sqlQueryGenres, new MapRowToGenre(), filmId);

                if (genres.size() > 0) {
                    film.setGenres(new HashSet<>());
                    genres.forEach(g -> film.getGenres().add(g));
                }

                String sqlQueryRating = "SELECT mpaa_id, mpaa_name FROM MPAA WHERE mpaa_id = ?";
                Optional<Mpaa> mpaa = Optional.ofNullable(
                        jdbcTemplate.queryForObject(sqlQueryRating, new MapRowToMpa(), film.getMpa().getId())
                );
                mpaa.ifPresent(film::setMpa);

                return Optional.of(film);
            }
            return result;
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Film addLike(User user, Film film) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), film.getId());

        Optional<Film> result =  getFilmById(film.getId());
        if (result.isEmpty()){
            return new Film();
        }
        return result.get();
    }

    @Override
    public Film deleteLike(User user, Film film) {
        String sqlQuery = "DELETE FROM likes WHERE (user_id, film_id) IN ((?, ?))";
        jdbcTemplate.update(sqlQuery, user.getId(), film.getId());
        Optional<Film> result = getFilmById(film.getId());
        if (result.isEmpty()){
            return new Film();
        }
        return result.get();
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> result = new ArrayList<>();

        String sqlQuery = "SELECT film_id, " +
                "f.film_name, " +
                "f.film_description, " +
                "f.film_release_date, " +
                "f.film_duration, " +
                "f.film_mpa_id,  " +
                "m.MPAA_NAME "+
                "FROM films AS f LEFT JOIN MPAA M on f.FILM_MPA_ID = M.MPAA_ID";

        SqlRowSet filmsRowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (filmsRowSet.next()){
            Film film = new Film();
            film.setId(filmsRowSet.getLong("film_id"));
            film.setName(filmsRowSet.getString("film_name"));
            film.setDescription(filmsRowSet.getString("film_description"));
            film.setReleaseDate(filmsRowSet.getDate("film_release_date").toLocalDate());
            film.setDuration(filmsRowSet.getInt("film_duration"));
            film.setMpa(new Mpaa(filmsRowSet.getInt("film_mpa_id"), filmsRowSet.getString("MPAA_NAME")));

            String sqlQueryGenres = "SELECT g.genre_id, g.genre_name FROM film_genres AS fg " +
                    "JOIN genres AS g ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";
            List<Genre> genres = jdbcTemplate.query(sqlQueryGenres, new MapRowToGenre(), film.getId());

            if (genres.size() > 0) {
                film.setGenres(new HashSet<>());
                genres.forEach(g -> film.getGenres().add(g));
            }
            result.add(film);

        }
        return result;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, f.film_duration, f.film_mpa_id " +
                "FROM films AS f " +
                "LEFT JOIN likes AS fl " +
                "ON f.film_id = fl.film_id " +
                "LEFT JOIN film_genres AS fg " +
                "ON fg.film_id = f.film_id " +
                " GROUP BY f.film_id " +
                "ORDER BY COUNT(DISTINCT fl.user_id) DESC LIMIT ?";
        List<Film> filmsFound = jdbcTemplate.query(sqlQuery, new MapRowToFilm(), count);
        List<Film> result = new ArrayList<>();
        filmsFound.forEach((f) -> result.add(getFilmById(f.getId()).get()));
        return result;
    }
}
