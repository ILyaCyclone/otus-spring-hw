package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Genre;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
}
