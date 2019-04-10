package cyclone.otusspring.library.dao.jdbc;

import cyclone.otusspring.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class BookDaoJdbcTest {

    @Autowired
    NamedParameterJdbcOperations jdbcOperations;
    BookDaoJdbc bookDaoJdbc;

    @BeforeEach
    void setUp() {
        this.bookDaoJdbc = new BookDaoJdbc(jdbcOperations);
    }

    @Test
    void findAll() {
        assertThat(bookDaoJdbc.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }

    @ParameterizedTest
    @MethodSource("findByTitleParameters")
    void findByTitle(String title, Book[] expected) {
        assertThat(bookDaoJdbc.findByTitle(title)).containsExactly(expected);
    }

    private static Stream<Arguments> findByTitleParameters() {
        return Stream.of(
                Arguments.of("air", new Book[]{BOOK2}),
                Arguments.of("it", new Book[]{BOOK5, BOOK3})
        );
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> bookDaoJdbc.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void findOne() {
        assertThat(bookDaoJdbc.findOne(2)).isEqualTo(BOOK2);
    }



    @Test
    void testInsert() {
        long savedId = bookDaoJdbc.save(NEW_BOOK).getBookId();

        Book actual = bookDaoJdbc.findOne(savedId);

        assertThat(actual.getBookId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_BOOK, "bookId");
    }

    @Test
    void testUpdate() {
        Book updatedBook2 = new Book(BOOK2.getBookId(), "Updated " + BOOK2.getTitle(), BOOK2.getYear() + 1, AUTHOR1, GENRE1);
        bookDaoJdbc.save(updatedBook2);

        Book actual = bookDaoJdbc.findOne(updatedBook2.getBookId());

        assertThat(actual).isEqualToComparingFieldByField(updatedBook2);
    }

    @Test
    @DisplayName("updating non existent book throws exception")
    void testUpdateNonExistent() {
        Book noSuchBook = new Book(NO_SUCH_ID, "No such", 2000, AUTHOR1, GENRE1);

        assertThatThrownBy(() -> bookDaoJdbc.save(noSuchBook)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void testDelete() {
        bookDaoJdbc.delete(BOOK2);
        assertThat(bookDaoJdbc.findAll()).containsExactly(BOOK5, BOOK4, BOOK3, BOOK1);
    }

    @Test
    void testDeleteById() {
        bookDaoJdbc.delete(BOOK1.getBookId());
        assertThat(bookDaoJdbc.findAll()).containsExactly(BOOK5, BOOK2, BOOK4, BOOK3);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> bookDaoJdbc.delete(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}