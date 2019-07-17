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
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import(GenreRepositoryImpl.class)
@ExtendWith(ResetStateExtension.class)
class GenreRepositoryImplTest {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAll() {
        StepVerifier.create(genreRepository.findAll())
                .expectSubscription()
                .expectNext(GENRE1, GENRE4, GENRE3, GENRE2, GENRE_WITHOUT_BOOKS)
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Genre[] expected) {
        StepVerifier.create(genreRepository.findByName(nameQuery))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("FICT", new Genre[]{GENRE2}),
                Arguments.of("ve", new Genre[]{GENRE1, GENRE3})
        );
    }

    @Test
    void findOne() {
        StepVerifier.create(genreRepository.findOne("2"))
                .expectSubscription()
                .expectNext(GENRE2)
                .verifyComplete();
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        StepVerifier.create(genreRepository.findOne(NO_SUCH_ID))
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();
    }



    @Test
    void testInsert() {
        genreRepository.save(NEW_GENRE)
                .map(Genre::getId)
                .subscribe(savedId ->
                        StepVerifier.create(genreRepository.findOne(savedId))
                                .expectSubscription()
                                .assertNext(actual -> {
                                    assertThat(actual.getId()).isNotNull();
                                    assertThat(actual).isEqualToIgnoringGivenFields(NEW_GENRE, "id");
                                })
                                .verifyComplete()
                );
    }

    @Test
    void testUpdate() {
        Genre updatedGenre2 = new Genre(GENRE2.getId(), "Updated " + GENRE2.getName());
        genreRepository.save(updatedGenre2)
                .subscribe(savedAuthor ->
                        StepVerifier.create(genreRepository.findOne(updatedGenre2.getId()))
                                .expectSubscription()
                                .expectNext(updatedGenre2));
    }

    @Test
    void testDelete() {
        genreRepository.delete(GENRE2.getId())
                .block();

        assertThat(mongoTemplate.findAll(Genre.class)).doesNotContain(GENRE2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> {
            genreRepository.delete(NO_SUCH_ID).block();
        }).isInstanceOf(NotFoundException.class);

//        StepVerifier.create(genreRepository.delete(NO_SUCH_ID))
//                .expectSubscription()
//                .expectError(NotFoundException.class)
//                .verify();
    }

    @ParameterizedTest
    @MethodSource("existsParameters")
    void exists(String genreId, boolean expected) {
        StepVerifier.create(genreRepository.exists(genreId))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> existsParameters() {
        return Stream.of(
                Arguments.of(GENRE2.getId(), true),
                Arguments.of(NO_SUCH_ID, false)
        );
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            genreRepository.save(new Genre(NEW_GENRE.getName())).block();
            genreRepository.save(new Genre(NEW_GENRE.getName())).block();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}