package cyclone.otusspring.library.dao.jdbc;

import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
@Profile(DataAccessProfiles.JDBC)
@Transactional(readOnly = true)
public class BookDaoJdbc implements BookDao {

    private static final RowMapper<Book> BOOK_ROW_MAPPER = (rs, rowNum) -> new Book(rs.getLong("book_id"), rs.getString("title")
            , ResultSetUtils.optInt(rs, "year")
            , new Author(rs.getLong("author_id"), rs.getString("author_firstname")
            , rs.getString("author_lastname"), rs.getString("author_homeland"))
            , new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));

    private static final String SELECT_ALL = "select b.book_id, b.title, b.year " +
            ", a.author_id, a.firstname as author_firstname, a.lastname as author_lastname, a.homeland as author_homeland " +
            ", g.genre_id, g.name as genre_name " +
            "from book b join author a on b.author_id = a.author_id " +
            "join genre g on b.genre_id = g.genre_id ";

    private final NamedParameterJdbcOperations jdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Book> findAll() {
        return jdbcOperations.query(SELECT_ALL + " order by b.title", BOOK_ROW_MAPPER);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jdbcOperations.query(SELECT_ALL +
                        " where lower(b.title) like '%'||lower(:title)||'%' " +
                        "order by b.title"
                , new MapSqlParameterSource("title", title)
                , BOOK_ROW_MAPPER);
    }

    @Override
    public Book findOne(long id) {
        return jdbcOperations.queryForObject(SELECT_ALL + " where book_id = :id"
                , new MapSqlParameterSource("id", id)
                , BOOK_ROW_MAPPER);

    }



    @Override
    @Transactional
    public Book save(Book book) {
        requireEntity(book);

        if (Objects.isNull(book.getBookId())) {
            return insert(book);
        } else {
            return update(book);
        }
    }



    @Override
    @Transactional
    public void delete(Book book) {
        requireEntity(book);
        Objects.requireNonNull(book.getBookId(), "Book ID must not be null");

        delete(book.getBookId());
    }

    @Override
    @Transactional
    public void delete(long id) {
        int affectedRows = jdbcOperations.
                update("delete book where book_id = :id"
                        , new MapSqlParameterSource("id", id));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 book with ID " + id, 1);
        }
    }



    @Override
    public boolean exists(long id) {
        Integer count = jdbcOperations.queryForObject("select count(book_id) from book where book_id = :id"
                , new MapSqlParameterSource("id", id)
                , Integer.class);
        return count > 0;
    }


    private Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into book(author_id, genre_id, title, year) values(:author_id, :genre_id, :title, :year)"
                , new MapSqlParameterSource().addValue("author_id", book.getAuthor().getAuthorId())
                        .addValue("genre_id", book.getGenre().getGenreId())
                        .addValue("title", book.getTitle())
                        .addValue("year", book.getYear())
                , keyHolder);

        return findOne(keyHolder.getKey().longValue());
    }

    private Book update(Book book) {
        int affectedRows = jdbcOperations.update("update book set author_id = :author_id, genre_id = :genre_id" +
                        ", title = :title, year = :year where book_id = :id"
                , new MapSqlParameterSource().addValue("author_id", book.getAuthor().getAuthorId())
                        .addValue("genre_id", book.getGenre().getGenreId())
                        .addValue("title", book.getTitle())
                        .addValue("year", book.getYear())
                        .addValue("id", book.getBookId()));
        if (affectedRows != 1) {
            throw new EmptyResultDataAccessException("Expected exactly 1 book with ID " + book.getBookId(), 1);
        }

        return findOne(book.getBookId());
    }


    private void requireEntity(Book book) {
        Objects.requireNonNull(book, "Book must not be null");
    }
}
