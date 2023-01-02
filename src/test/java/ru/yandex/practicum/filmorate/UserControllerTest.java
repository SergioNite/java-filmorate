package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private final MockMvc mock;

    @Autowired
    public UserControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @Test
    @DisplayName("Create user")
    public void shouldAddUser() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@user.ru\", " +
                                "\"login\": \"testuser\", " +
                                "\"name\": \"Test User\"," +
                                "\"birthday\": \"1980-01-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.email").value("test@user.ru"))
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.birthday").value("1980-01-01"));
    }

    @Test
    @DisplayName("Update user")
    public void shouldUpdateUser() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@user.ru\", " +
                                "\"login\": \"testuser\", " +
                                "\"name\": \"Test User\"," +
                                "\"birthday\": \"1980-01-01\"}"))
                .andExpect(status().isCreated());

        mock.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"email\": \"test555@user.ru\", " +
                                "\"login\": \"testuser555\", " +
                                "\"name\": \"Test User 555\"," +
                                "\"birthday\": \"1955-05-05\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.email").value("test555@user.ru"))
                .andExpect(jsonPath("$.login").value("testuser555"))
                .andExpect(jsonPath("$.name").value("Test User 555"))
                .andExpect(jsonPath("$.birthday").value("1955-05-05"));
    }
}
