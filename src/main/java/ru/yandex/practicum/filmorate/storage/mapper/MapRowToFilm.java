package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpaa;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MapRowToFilm implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film result = new Film();
        result.setId(rs.getLong("film_id"));
        result.setName(rs.getString("film_name"));
        result.setDescription(rs.getString("film_description"));
        result.setReleaseDate(rs.getDate("film_release_date").toLocalDate());
        result.setDuration(rs.getInt("film_duration"));
        Mpaa rating = new Mpaa();
        rating.setId(rs.getInt("film_mpa_id"));
        if (hasColumn(rs,"MPAA_NAME")) {
            rating.setName(rs.getString("mpaa_name"));
        }
        result.setMpa(rating);
        return result;
    }

    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}
