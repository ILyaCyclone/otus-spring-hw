package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;
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
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2
// , replace=Replace.NONE)
class AuthorDaoJdbcTest {

    private static final Author AUTHOR1 = new Author(1L, "Test Arthur", "Hailey", "Canada");
    private static final Author AUTHOR2 = new Author(2L, "Test Isaac", "Asimov", "Russia");
    private static final Author AUTHOR3 = new Author(3L, "Test Gabriel", "Marquez", "Argentina");
    private static final long NO_SUCH_ID = 999;

    @Autowired
    NamedParameterJdbcOperations jdbcOperations;
    AuthorDaoJdbc authorDaoJdbc;

    @BeforeEach
    void setUp() {
        this.authorDaoJdbc = new AuthorDaoJdbc(jdbcOperations);
    }

    @Test
    void findAll() {
        assertThat(authorDaoJdbc.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2); // 1, 3, 2 because of ordering
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Author[] expected) {
        assertThat(authorDaoJdbc.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("gabri", new Author[]{AUTHOR3}),
                Arguments.of("ar", new Author[]{AUTHOR1, AUTHOR3})
        );
    }

    @Test
    void findOne() {
        assertThat(authorDaoJdbc.findOne(2)).isEqualTo(AUTHOR2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> authorDaoJdbc.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}