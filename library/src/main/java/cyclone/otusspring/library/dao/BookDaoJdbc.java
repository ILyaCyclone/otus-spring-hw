package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class BookDaoJdbc implements BookDao {

    private static final BeanPropertyRowMapper<Book> BOOK_ROW_MAPPER = new BeanPropertyRowMapper(Book.class);

    private final NamedParameterJdbcOperations jdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Book> findAll() {
        return jdbcOperations.query("select book_id, author_id, genre_id, title, year from book order by title", BOOK_ROW_MAPPER);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jdbcOperations.query("select book_id, author_id, genre_id, title, year from book " +
                        "where lower(title) like '%'||lower(:title)||'%' " +
                        "order by title"
                , new MapSqlParameterSource("title", title)
                , BOOK_ROW_MAPPER);
    }

    @Override
    public Book findOne(long id) {
        return jdbcOperations.queryForObject("select book_id, author_id, genre_id, title, year from book where book_id = :id"
                , new MapSqlParameterSource("id", id)
                , BOOK_ROW_MAPPER);

    }
}
