package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpaa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToMpa implements RowMapper<Mpaa> {
    @Override
    public Mpaa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpaa result = new Mpaa();
        result.setId(rs.getInt("mpaa_id"));
        result.setName(rs.getString("mpaa_name"));
        return result;
    }
}
