package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;
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

    @Override
    public Author save(Author author) {
        requireEntity(author);

        if (Objects.isNull(author.getAuthorId())) {
            return insert(author);
        } else {
            update(author);
            return author;
        }
    }



    @Override
    public void delete(Author author) {
        requireEntity(author);

        delete(author.getAuthorId());
    }

    @Override
    public void delete(long id) {
        int affectedRows = jdbcOperations.
                update("delete author where author_id = :id"
                        , new MapSqlParameterSource("id", id));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 author with ID " + id, 1);
        }
    }


    private Author insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into author(firstname, lastname, homeland) values(:firstname, :lastname, :homeland)"
                , new MapSqlParameterSource().addValue("firstname", author.getFirstname())
                        .addValue("lastname", author.getLastname())
                        .addValue("homeland", author.getHomeland())
                , keyHolder);

        return new Author(keyHolder.getKey().longValue(), author.getFirstname(), author.getLastname(), author.getHomeland());
    }

    private void update(Author author) {
        int affectedRows = jdbcOperations.update("update author set firstname = :firstname, lastname = :lastname, homeland = :homeland where author_id = :id"
                , new MapSqlParameterSource().addValue("firstname", author.getFirstname())
                        .addValue("lastname", author.getLastname())
                        .addValue("homeland", author.getHomeland())
                        .addValue("id", author.getAuthorId()));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 author with ID " + author.getAuthorId(), 1);
        }
    }


    private void requireEntity(Author author) {
        Objects.requireNonNull(author, "Author must not be null");
    }
}
