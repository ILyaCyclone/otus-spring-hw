package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class GenreDaoJdbc implements GenreDao {

    private static final BeanPropertyRowMapper<Genre> GENRE_ROW_MAPPER = new BeanPropertyRowMapper(Genre.class);

    private final NamedParameterJdbcOperations jdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcOperations.query("select genre_id, name from genre order by name"
                , GENRE_ROW_MAPPER);
    }

    @Override
    public List<Genre> findByName(String name) {
        return jdbcOperations.query("select genre_id, name from genre " +
                        "where lower(name) like '%'||lower(:name)||'%' " +
                        "order by name"
                , new MapSqlParameterSource("name", name)
                , GENRE_ROW_MAPPER);
    }

    @Override
    public Genre findOne(long id) {
        return jdbcOperations.queryForObject("select genre_id, name from genre " +
                        "where genre_id = :id"
                , new MapSqlParameterSource("id", id)
                , GENRE_ROW_MAPPER);
    }



    @Override
    public Genre save(Genre genre) {
        requireEntity(genre);

        if (Objects.isNull(genre.getGenreId())) {
            return insert(genre);
        } else {
            update(genre);
            return genre;
        }
    }



    @Override
    public void delete(Genre genre) {
        requireEntity(genre);
        Objects.requireNonNull(genre.getGenreId(), "Genre ID must not be null");

        delete(genre.getGenreId());
    }

    @Override
    public void delete(long id) {
        int affectedRows = jdbcOperations.
                update("delete genre where genre_id = :id"
                        , new MapSqlParameterSource("id", id));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 genre with ID " + id, 1);
        }
    }


    private Genre insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into genre(name) values(:name)"
                , new MapSqlParameterSource().addValue("name", genre.getName())
                , keyHolder);

        return new Genre(keyHolder.getKey().longValue(), genre.getName());
    }

    private void update(Genre genre) {
        int affectedRows = jdbcOperations.update("update genre set name = :name " +
                        "where genre_id = :id"
                , new MapSqlParameterSource().addValue("name", genre.getName())
                        .addValue("id", genre.getGenreId()));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 genre with ID " + genre.getGenreId(), 1);
        }
    }


    private void requireEntity(Genre genre) {
        Objects.requireNonNull(genre, "Genre must not be null");
    }
}
