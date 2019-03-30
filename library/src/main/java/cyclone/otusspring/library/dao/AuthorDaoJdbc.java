package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private static final BeanPropertyRowMapper<Author> AUTHOR_ROW_MAPPER = new BeanPropertyRowMapper(Author.class);

    private final NamedParameterJdbcOperations jdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query("select author_id, firstname, lastname, homeland from author order by firstname, lastname"
                , AUTHOR_ROW_MAPPER);
    }

    @Override
    public List<Author> findByName(String name) {
        return jdbcOperations.query("select author_id, firstname, lastname, homeland from author " +
                        "where lower(firstname) like '%'||lower(:name)||'%' " +
                        "or lower(lastname) like '%'||lower(:name)||'%' " +
                        "order by firstname, lastname"
                , new MapSqlParameterSource("name", name)
        , AUTHOR_ROW_MAPPER);
    }

    @Override
    public Author findOne(long id) {
        return jdbcOperations.queryForObject("select author_id, firstname, lastname, homeland from author " +
                        "where author_id = :id"
                , new MapSqlParameterSource("id", id)
                , AUTHOR_ROW_MAPPER);
    }
}
