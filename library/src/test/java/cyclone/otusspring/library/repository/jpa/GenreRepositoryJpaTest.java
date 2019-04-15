package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.repository.jpa")
class GenreRepositoryJpaTest {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    TestEntityManager tem;


    @Test
    void findAll() {
        assertThat(genreRepository.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2);
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Genre[] expected) {
        assertThat(genreRepository.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("FICT", new Genre[]{GENRE2}),
                Arguments.of("ve", new Genre[]{GENRE1, GENRE3})
        );
    }

    @Test
    void findOne() {
        assertThat(genreRepository.findOne(2)).isEqualTo(GENRE2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> genreRepository.findOne(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }



    @Test
    void testInsert() {
        long savedId = genreRepository.save(NEW_GENRE).getGenreId();

        Genre actual = genreRepository.findOne(savedId);

        assertThat(actual.getGenreId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_GENRE, "genreId");
    }

    @Test
    void testUpdate() {
        Genre updatedGenre2 = new Genre(GENRE2.getGenreId(), "Updated " + GENRE2.getName());
        genreRepository.save(updatedGenre2);

        Genre actual = genreRepository.findOne(updatedGenre2.getGenreId());

        assertThat(actual).isEqualToComparingFieldByField(updatedGenre2);
    }

    @Test
    void testDelete() {
        Genre bookToDelete = tem.find(Genre.class, GENRE2.getGenreId());

        genreRepository.delete(bookToDelete);
        assertThat(genreRepository.findAll()).doesNotContain(GENRE2);
    }

    @Test
    void testDeleteById() {
        genreRepository.delete(GENRE1.getGenreId());
        assertThat(genreRepository.findAll()).doesNotContain(GENRE1);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> genreRepository.delete(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testExistTrue() {
        assertThat(genreRepository.exists(GENRE2.getGenreId())).isTrue();
    }

    @Test
    void testExistFalse() {
        assertThat(genreRepository.exists(NO_SUCH_ID)).isFalse();
    }
}