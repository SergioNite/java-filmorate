package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    private final MockMvc mock;
    @Autowired
    public FilmControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @Test
    @DisplayName("Film create")
    public void shouldAddFilm() throws Exception {
        mock.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, " +
                                "\"name\": \"Дюна\", " +
                                "\"description\": \"Фантастический фильм по мотивам романа Фрэнка Герберта\", " +
                                "\"releaseDate\": \"1984-12-03\"," +
                                "\"duration\": 137}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value("Дюна"))
                .andExpect(jsonPath("$.description").value("Фантастический фильм по мотивам романа Фрэнка Герберта"))
                .andExpect(jsonPath("$.releaseDate").value("1984-12-03"))
                .andExpect(jsonPath("$.duration").value(137));
    }


    @Test
    @DisplayName("Film create Fail name")
    public void CreateFilmWithFailName() throws Exception {
        mock.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, " +
                                "\"name\": \"\", " +
                                "\"description\": \"Фантастический фильм по мотивам романа Фрэнка Герберта\", " +
                                "\"releaseDate\": \"1984-12-03\"," +
                                "\"duration\": 137}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film create Fail description")
    public void CreateFilmWithFailDescrition() throws Exception {
        mock.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, " +
                                "\"name\": \"Film name\", " +
                                "\"description\": \"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                                "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал " +
                                "кандидатом Коломбани.\", " +
                                "\"releaseDate\": \"1984-12-03\"," +
                                "\"duration\": 137}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film create Fail releaseDate")
    public void CreateFilmWithFailReleaseDate() throws Exception {
        mock.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, " +
                                "\"name\": \"Name\", " +
                                "\"description\": \"Description\", " +
                                "\"releaseDate\": \"1890-03-25\"," +
                                "\"duration\": 200}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film create Fail duration")
    public void CreateFilmWithFailDuration() throws Exception {
        mock.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, " +
                                "\"name\": \"Name\", " +
                                "\"description\": \"Description\", " +
                                "\"releaseDate\": \"1984-12-03\"," +
                                "\"duration\": -200}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film update")
    public void shouldUpdateFilm() throws Exception {
        mock.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, " +
                        "\"name\": \"Заголовок\", " +
                        "\"description\": \"Описание\", " +
                        "\"releaseDate\": \"1990-01-01\"," +
                        "\"duration\": 100}"));


        mock.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"name\": \"test\", " +
                                "\"description\": \"test\", " +
                                "\"releaseDate\": \"2023-01-01\","  +
                                "\"duration\": 100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.releaseDate").value("2023-01-01"))
                .andExpect(jsonPath("$.duration").value(100));
    }

    @Test
    @DisplayName("Film update unknown")
    public void shouldUpdateFilmUnknown() throws Exception {
        mock.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 9999, " +
                                "\"name\": \"Film Updated\", " +
                                "\"description\": \"New film update decription\", " +
                                "\"releaseDate\": \"1989-04-17\","  +
                                "\"duration\": 190}"))
                .andExpect(status().isBadRequest());
    }
}
