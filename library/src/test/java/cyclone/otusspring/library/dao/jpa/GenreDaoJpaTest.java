package cyclone.otusspring.library.dao.jpa;

import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.dao.GenreDao;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.dao.jpa")
@ActiveProfiles(DataAccessProfiles.JPA)
class GenreDaoJpaTest {

    @Autowired
    GenreDao genreDao;

    @Autowired
    TestEntityManager tem;


    @Test
    void findAll() {
        assertThat(genreDao.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2);
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Genre[] expected) {
        assertThat(genreDao.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("FICT", new Genre[]{GENRE2}),
                Arguments.of("ve", new Genre[]{GENRE1, GENRE3})
        );
    }

    @Test
    void findOne() {
        assertThat(genreDao.findOne(2)).isEqualTo(GENRE2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> genreDao.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }



    @Test
    void testInsert() {
        long savedId = genreDao.save(NEW_GENRE).getGenreId();

        Genre actual = genreDao.findOne(savedId);

        assertThat(actual.getGenreId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_GENRE, "genreId");
    }

    @Test
    void testUpdate() {
        Genre updatedGenre2 = new Genre(GENRE2.getGenreId(), "Updated " + GENRE2.getName());
        genreDao.save(updatedGenre2);

        Genre actual = genreDao.findOne(updatedGenre2.getGenreId());

        assertThat(actual).isEqualToComparingFieldByField(updatedGenre2);
    }

    @Test
    void testDelete() {
        Genre bookToDelete = tem.find(Genre.class, GENRE2.getGenreId());

        genreDao.delete(bookToDelete);
        assertThat(genreDao.findAll()).containsExactly(GENRE1, GENRE4, GENRE3);
    }

    @Test
    void testDeleteById() {
        genreDao.delete(GENRE1.getGenreId());
        assertThat(genreDao.findAll()).containsExactly(GENRE4, GENRE3, GENRE2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> genreDao.delete(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testExistTrue() {
        assertThat(genreDao.exists(GENRE2.getGenreId())).isTrue();
    }

    @Test
    void testExistFalse() {
        assertThat(genreDao.exists(NO_SUCH_ID)).isFalse();
    }
}