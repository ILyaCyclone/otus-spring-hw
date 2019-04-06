package cyclone.otusspring.library.dao.jdbc;

import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dto.BookDetails;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
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



    @Override
    public Book save(Book book) {
        requireEntity(book);

        if (Objects.isNull(book.getBookId())) {
            return insert(book);
        } else {
            update(book);
            return book;
        }
    }



    @Override
    public void delete(Book book) {
        requireEntity(book);
        Objects.requireNonNull(book.getBookId(), "Book ID must not be null");

        delete(book.getBookId());
    }

    @Override
    public void delete(long id) {
        int affectedRows = jdbcOperations.
                update("delete book where book_id = :id"
                        , new MapSqlParameterSource("id", id));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 book with ID " + id, 1);
        }
    }



    @Override
    public List<BookDetails> findAllWithDetails() {
        return jdbcOperations.query("select b.book_id, b.title, b.year " +
                ", a.author_id, a.firstname as author_firstname, a.lastname as author_lastname, a.homeland as author_homeland " +
                ", g.genre_id, g.name as genre_name " +
                "from book b join author a on b.author_id = a.author_id " +
                "join genre g on b.genre_id = g.genre_id " +
                "order by b.title", (rs, rowNum) -> new BookDetails(rs.getLong("book_id"), rs.getString("title")
                , ResultSetUtils.optInt(rs, "year")
                , new Author(rs.getLong("author_id"), rs.getString("author_firstname")
                , rs.getString("author_lastname"), rs.getString("author_homeland"))
                , new Genre(rs.getLong("genre_id"), rs.getString("genre_name")))
        );
    }



    private Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into book(author_id, genre_id, title, year) values(:author_id, :genre_id, :title, :year)"
                , new MapSqlParameterSource().addValue("author_id", book.getAuthorId())
                        .addValue("genre_id", book.getGenreId())
                        .addValue("title", book.getTitle())
                        .addValue("year", book.getYear())
                , keyHolder);

        return new Book(keyHolder.getKey().longValue(), book.getAuthorId(), book.getGenreId(), book.getTitle(), book.getYear());
    }

    private void update(Book book) {
        int affectedRows = jdbcOperations.update("update book set author_id = :author_id, genre_id = :genre_id" +
                        ", title = :title, year = :year where book_id = :id"
                , new MapSqlParameterSource().addValue("author_id", book.getAuthorId())
                        .addValue("genre_id", book.getGenreId())
                        .addValue("title", book.getTitle())
                        .addValue("year", book.getYear())
                        .addValue("id", book.getBookId()));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 book with ID " + book.getBookId(), 1);
        }
    }


    private void requireEntity(Book book) {
        Objects.requireNonNull(book, "Book must not be null");
    }
}
