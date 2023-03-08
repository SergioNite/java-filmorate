package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GenreStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        String SELECT_REQUEST = "select * from GENRES where genre_id =  ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_REQUEST, id);
        if (rs.next()) {
            return Optional.of(new Genre(rs.getInt(1),
                    rs.getString(2)));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.queryForStream("select * from GENRES order by GENRE_ID",
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("genre_name"))).collect(Collectors.toList());

    }
}
