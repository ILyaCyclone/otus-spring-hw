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

import static cyclone.otusspring.library.dao.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class GenreDaoJdbcTest {

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



    @Test
    void testInsert() {
        long savedId = genreDaoJdbc.save(NEW_GENRE).getGenreId();

        Genre actual = genreDaoJdbc.findOne(savedId);

        assertThat(actual.getGenreId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_GENRE, "genreId");
    }

    @Test
    void testUpdate() {
        Genre updatedGenre2 = new Genre(GENRE2.getGenreId(), "Updated " + GENRE2.getName());
        genreDaoJdbc.save(updatedGenre2);

        Genre actual = genreDaoJdbc.findOne(updatedGenre2.getGenreId());

        assertThat(actual).isEqualToComparingFieldByField(updatedGenre2);
    }

    @Test
    @DisplayName("updating non existent genre throws exception")
    void testUpdateNonExistent() {
        Genre noSuchGenre = new Genre(NO_SUCH_ID, "No such");

        assertThatThrownBy(() -> genreDaoJdbc.save(noSuchGenre)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void testDelete() {
        genreDaoJdbc.delete(GENRE2);
        assertThat(genreDaoJdbc.findAll()).containsExactly(GENRE1, GENRE4, GENRE3);
    }

    @Test
    void testDeleteById() {
        genreDaoJdbc.delete(GENRE1.getGenreId());
        assertThat(genreDaoJdbc.findAll()).containsExactly(GENRE4, GENRE3, GENRE2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> genreDaoJdbc.delete(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}