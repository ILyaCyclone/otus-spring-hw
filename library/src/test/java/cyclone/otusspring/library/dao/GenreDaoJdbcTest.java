package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Genre;
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
class GenreDaoJdbcTest {

    private static final Genre GENRE1 = new Genre(1L, "Test Adventures");
    private static final Genre GENRE2 = new Genre(2L, "Test Science fiction");
    private static final Genre GENRE3 = new Genre(3L, "Test Novel");
    private static final Genre GENRE4 = new Genre(4L, "Test Magic realism");
    private static final long NO_SUCH_ID = 999;

    @Autowired
    NamedParameterJdbcOperations jdbcOperations;
    GenreDaoJdbc genreDaoJdbc;

    @BeforeEach
    void setUp() {
        this.genreDaoJdbc = new GenreDaoJdbc(jdbcOperations);
    }

    @Test
    void findAll() {
        assertThat(genreDaoJdbc.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2);
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Genre[] expected) {
        assertThat(genreDaoJdbc.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("FICT", new Genre[]{GENRE2}),
                Arguments.of("ve", new Genre[]{GENRE1, GENRE3})
        );
    }

    @Test
    void findOne() {
        assertThat(genreDaoJdbc.findOne(2)).isEqualTo(GENRE2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> genreDaoJdbc.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}