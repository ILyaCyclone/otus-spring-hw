package cyclone.otusspring.library.dao;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class BookDaoJdbcTest {

    private static final Book BOOK1 = new Book(1L, 1, 1, "Test Wheels", 1971);
    private static final Book BOOK2 = new Book(2L, 1, 1, "Test Airport", 1968);
    private static final Book BOOK3 = new Book(3L, 2, 2, "Test The End of Eternity", 1955);
    private static final Book BOOK4 = new Book(4L, 2, 2, "Test Foundation", 1951);
    private static final Book BOOK5 = new Book(5L, 3, 3, "Test 100 Years of Solitude", 1967);
    private static final long NO_SUCH_ID = 999;

    @Autowired
    NamedParameterJdbcOperations jdbcOperations;
    BookDaoJdbc bookDaoJdbc;

    @BeforeEach
    void setUp() {
        this.bookDaoJdbc = new BookDaoJdbc(jdbcOperations);
    }

    @Test
    void findAll() {
        assertThat(bookDaoJdbc.findAll()).containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
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
}