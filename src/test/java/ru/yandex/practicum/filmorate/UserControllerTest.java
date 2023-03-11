package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FilmorateApplication.class)
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
        mock.perform(delete("/users/1"));
        mock.perform(delete("/users/2"));
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
    @DisplayName("Create user with empty name")
    public void shouldAddUserWithEmptyName() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"friend@common.ru\", " +
                                "\"login\": \"common\", " +
                                "\"name\": \"Nick Name\","+
                                "\"birthday\": \"2000-08-20\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.email").value("friend@common.ru"))
                .andExpect(jsonPath("$.login").value("common"))
                .andExpect(jsonPath("$.birthday").value("2000-08-20"));
    }

    @Test
    @DisplayName("Create user Fail login")
    public void shouldNotAddUserWithFailLogin() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@user.ru\", " +
                                "\"login\": \"dolore ullamco\", " +
                                "\"birthday\": \"1980-01-01\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user Fail email")
    public void shouldNotAddUserWithFailEmail() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail.ru\", " +
                                "\"login\": \"testuser\", " +
                                "\"name\": \"\"," +
                                "\"birthday\": \"1980-08-20\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user Fail birthday")
    public void shouldNotAddUserWithFailBirthday() throws Exception {
        mock.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@mail.ru\", " +
                                "\"login\": \"dolore\", " +
                                "\"name\": \"\"," +
                                "\"birthday\": \"2446-08-20\"}"))
                .andExpect(status().isBadRequest());
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

    @Test
    @DisplayName("Update user Update Unknown")
    public void shouldNotUpdateUnknownUser() throws Exception {
        mock.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 9999, " +
                                "\"email\": \"mail@yandex.ru\", " +
                                "\"login\": \"doloreUpdate\", " +
                                "\"name\": \"est adipisicing\"," +
                                "\"birthday\": \"1976-09-20\"}"))
                .andExpect(status().isNotFound());
    }
}
