package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmStorageTest {

    private final FilmStorage filmStorage;
    private Film film = null;
    private Film newFilm;

    @BeforeEach
    public void addFilm() {
        Film film = new Film();
        film.setName("Дюна");
        film.setDescription("Капитул Дюны");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);
        film.setMpa(new Mpaa(1, "G"));
        film.setGenres(Set.of(new Genre(1, null)));
        this.film = film;
        this.newFilm = filmStorage.addFilm(film);
    }

    @Test
    public void createFilm() {
        assertThat(newFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(newFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(newFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(newFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(newFilm).hasFieldOrPropertyWithValue("duration", film.getDuration());
        assertThat(newFilm).hasFieldOrPropertyWithValue("mpa", film.getMpa());

        assertEquals(newFilm.getGenres().size(), 1);
        assertEquals(new ArrayList<>(newFilm.getGenres()).get(0).getName(), "Комедия");
        assertEquals(newFilm.getMpa().getId(), film.getMpa().getId());
        assertEquals(newFilm.getMpa().getName(), "G");
    }

    @Test
    public void getFilmById() {
        Optional<Film> testFilm1 = filmStorage.getFilmById(newFilm.getId());

        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("name", newFilm.getName());
        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("description", newFilm.getDescription());
        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("releaseDate", newFilm.getReleaseDate());
        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("duration", newFilm.getDuration());
        assertThat(testFilm1.get()).hasFieldOrPropertyWithValue("mpa", newFilm.getMpa());

        assertEquals(testFilm1.get().getGenres().size(), 1);
        assertEquals(new ArrayList<>(testFilm1.get().getGenres()).get(0).getName(), "Комедия");
        assertEquals(testFilm1.get().getMpa().getId(), newFilm.getMpa().getId());
        assertEquals(testFilm1.get().getMpa().getName(), "G");
    }

    @Test
    public void getAllFilms() {
        Collection<Film> films = filmStorage.getAll();

        assertEquals(films.size(), 1);
    }

    @Test
    public void updateFilm() {
        newFilm.setName("ddd");
        Optional<Film> updatedFilm = filmStorage.updateFilm(newFilm.getId(), newFilm);
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("name", "ddd");
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("description", newFilm.getDescription());
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("releaseDate", newFilm.getReleaseDate());
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("duration", newFilm.getDuration());
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("mpa", newFilm.getMpa());

        assertEquals(updatedFilm.get().getGenres().size(), 1);
        assertEquals(new ArrayList<>(updatedFilm.get().getGenres()).get(0).getName(), "Комедия");
        assertEquals(updatedFilm.get().getMpa().getId(), newFilm.getMpa().getId());
        assertEquals(updatedFilm.get().getMpa().getName(), "G");
    }
}
