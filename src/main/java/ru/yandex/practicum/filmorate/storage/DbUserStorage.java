package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.MapRowToUser;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@Qualifier("DbUserStorage")
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> addUser(User user) {

        if (user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        String sqlQuery = "INSERT INTO users (user_email, user_login, user_name, user_birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int userId = keyHolder.getKey().intValue();
        String sqlQuerySearch = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id = ?";
        User result = jdbcTemplate.queryForObject(sqlQuerySearch, new MapRowToUser(), userId);
        return Optional.of(result);
    }

    @Override
    public Optional<User> updateUser(long userId, User user) {
        try {
            String sqlQuerySearch = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id = ?";
            User result = jdbcTemplate.queryForObject(sqlQuerySearch, new MapRowToUser(), userId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        String sqlQuery = "UPDATE users SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        String sqlQuerySecondSearch = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuerySecondSearch, new MapRowToUser(), userId));
    }

    @Override
    public Optional<User> deleteUser(long userId) {
        User result;
        try {
            String sqlQuerySearch = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id = ?";
            result = jdbcTemplate.queryForObject(sqlQuerySearch, new MapRowToUser(), userId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        String sqlQuery = "DELETE FROM users where user_id = ?";
        jdbcTemplate.update(sqlQuery, userId);
        assert result != null;
        return Optional.of(result);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        try {
            String sqlQuery = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new MapRowToUser(), userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users";
        return jdbcTemplate.query(sqlQuery, new MapRowToUser());
    }

    @Override
    public List<User> addFriend(long id, long friendId) {
        Optional<User> first = getUserById(id);
        Optional<User> second = getUserById(friendId);
        if (first.isEmpty() || second.isEmpty()){
            return new ArrayList<>();
        }
        String sqlQuery = "INSERT INTO FRIENDS VALUES (?, ?, true)";
        jdbcTemplate.update(sqlQuery, first.get().getId(), second.get().getId());
        return List.of(first.get(), second.get());

    }

    @Override
    public List<User> deleteFriend(long id, long friendId) {
        Optional<User> first = getUserById(id);
        Optional<User> second = getUserById(friendId);
        if (first.isEmpty() || second.isEmpty()){
            return new ArrayList<>();
        }
        String sqlQuery = "DELETE FROM FRIENDS WHERE (user_id, friend_id) IN ((?, ?)) AND VALIDATED = true";
        jdbcTemplate.update(sqlQuery, first.get().getId(), second.get().getId());
        return List.of(first.get(), second.get());
    }

    @Override
    public List<User> getFriends(long userId) {
        Optional<User> user = getUserById(userId);
        if (user.isEmpty()) return new ArrayList<>();

        try {
            String sqlQuery = "SELECT user_id, user_email, user_login, user_name, user_birthday FROM users WHERE user_id IN " +
                    "(SELECT friend_id FROM friends WHERE user_id = ? AND VALIDATED = true)";
            return jdbcTemplate.query(sqlQuery, new MapRowToUser(), userId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        User first = getUserById(userId).get();
        User second = getUserById(friendId).get();
        List<User> result = new ArrayList<>();
        try {
            String sqlQuery = "SELECT user_id, user_email, user_login, user_name, user_birthday " +
                    "FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ? AND VALIDATED = true " +
                    "INTERSECT SELECT friend_id FROM friends WHERE user_id = ? AND VALIDATED = true)";
            return jdbcTemplate.query(sqlQuery, new MapRowToUser(), userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
    }
}
