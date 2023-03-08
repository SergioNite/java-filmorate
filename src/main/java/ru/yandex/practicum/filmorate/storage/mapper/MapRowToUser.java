package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToUser implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User result = new User();
        result.setId(rs.getLong("user_id"));
        result.setEmail(rs.getString("user_email"));
        result.setLogin(rs.getString("user_login"));
        result.setName(rs.getString("user_name"));
        result.setBirthday(rs.getDate("user_birthday").toLocalDate());
        return result;
    }
}
