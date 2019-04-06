package cyclone.otusspring.library.dao.jdbc;

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

import static cyclone.otusspring.library.dao.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace=Replace.NONE)
class AuthorDaoJdbcTest {

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



    @Test
    void testInsert() {
        long savedId = authorDaoJdbc.save(NEW_AUTHOR).getAuthorId();

        Author actual = authorDaoJdbc.findOne(savedId);

        assertThat(actual.getAuthorId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_AUTHOR, "authorId");
    }

    @Test
    void testUpdate() {
        Author updatedAuthor2 = new Author(AUTHOR2.getAuthorId(), "Updated " + AUTHOR2.getFirstname(), "Updated " + AUTHOR2.getLastname(), "Updated " + AUTHOR2.getHomeland());
        authorDaoJdbc.save(updatedAuthor2);

        Author actual = authorDaoJdbc.findOne(updatedAuthor2.getAuthorId());

        assertThat(actual).isEqualToComparingFieldByField(updatedAuthor2);
    }

    @Test
    @DisplayName("updating non existent author throws exception")
    void testUpdateNonExistent() {
        Author noSuchAuthor = new Author(NO_SUCH_ID, "No such", "No such", "No such");

        assertThatThrownBy(() -> authorDaoJdbc.save(noSuchAuthor)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void testDelete() {
        authorDaoJdbc.delete(AUTHOR2);
        assertThat(authorDaoJdbc.findAll()).containsExactly(AUTHOR1, AUTHOR3);
    }

    @Test
    void testDeleteById() {
        authorDaoJdbc.delete(AUTHOR1.getAuthorId());
        assertThat(authorDaoJdbc.findAll()).containsExactly(AUTHOR3, AUTHOR2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> authorDaoJdbc.delete(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}