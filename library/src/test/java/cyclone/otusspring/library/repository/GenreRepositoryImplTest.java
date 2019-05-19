package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@ComponentScan("cyclone.otusspring.library.repository")
@ExtendWith(ResetStateExtension.class)
class GenreRepositoryImplTest {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    MongoTemplate mongoTemplate;

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
        assertThat(genreRepository.findOne("2")).isEqualTo(GENRE2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> genreRepository.findOne(NO_SUCH_ID)).isInstanceOf(NotFoundException.class);
    }



    @Test
    void testInsert() {
        String savedId = genreRepository.save(NEW_GENRE).getId();

        Genre actual = genreRepository.findOne(savedId);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_GENRE, "id");
    }

    @Test
    void testUpdate() {
        Genre updatedGenre2 = new Genre(GENRE2.getId(), "Updated " + GENRE2.getName());
        genreRepository.save(updatedGenre2);

        Genre actual = genreRepository.findOne(updatedGenre2.getId());

        assertThat(actual).isEqualToComparingFieldByField(updatedGenre2);
    }

    @Test
    void testDelete() {
        Genre bookToDelete = mongoTemplate.findById(GENRE2.getId(), Genre.class);

        genreRepository.delete(bookToDelete);
        assertThat(genreRepository.findAll()).doesNotContain(GENRE2);
    }

    @Test
    void testDeleteById() {
        genreRepository.delete(GENRE1.getId());
        assertThat(genreRepository.findAll()).doesNotContain(GENRE1);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> genreRepository.delete(NO_SUCH_ID)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testExistsTrue() {
        assertThat(genreRepository.exists(GENRE2.getId())).isTrue();
    }

    @Test
    void testExistsFalse() {
        assertThat(genreRepository.exists(NO_SUCH_ID)).isFalse();
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            genreRepository.save(new Genre(NEW_GENRE.getName()));
            genreRepository.save(new Genre(NEW_GENRE.getName()));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}