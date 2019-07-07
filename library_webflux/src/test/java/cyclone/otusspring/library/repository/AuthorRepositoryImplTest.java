package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
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
@Import({AuthorRepositoryImpl.class})
@ExtendWith(ResetStateExtension.class)
class AuthorRepositoryImplTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAll() {
        StepVerifier.create(authorRepository.findAll())
                .expectSubscription()
                .expectNext(AUTHOR1, AUTHOR3, AUTHOR2, AUTHOR_WITHOUT_BOOKS)
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Author[] expected) {
        StepVerifier.create(authorRepository.findByName(nameQuery))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("GabrI", new Author[]{AUTHOR3}),
                Arguments.of("aR", new Author[]{AUTHOR1, AUTHOR3})
        );
    }

    @Test
    void findOne() {
        StepVerifier.create(authorRepository.findOne("2"))
                .expectSubscription()
                .expectNext(AUTHOR2)
                .verifyComplete();
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        StepVerifier.create(authorRepository.findOne(NO_SUCH_ID))
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();
    }



    @Test
    void testInsert() {
        authorRepository.save(NEW_AUTHOR)
                .map(Author::getId)
                .subscribe(savedId ->
                        StepVerifier.create(authorRepository.findOne(savedId))
                                .expectSubscription()
                                .assertNext(actual -> {
                                    assertThat(actual.getId()).isNotNull();
                                    assertThat(actual).isEqualToIgnoringGivenFields(NEW_AUTHOR, "id");
                                })
                                .verifyComplete()
                );
    }

    @Test
    void testUpdate() {
        Author updatedAuthor2 = new Author(AUTHOR2.getId(), "Updated " + AUTHOR2.getFirstname(), "Updated " + AUTHOR2.getLastname(), "Updated " + AUTHOR2.getHomeland());
        authorRepository.save(updatedAuthor2)
                .subscribe(savedAuthor ->
                        StepVerifier.create(authorRepository.findOne(updatedAuthor2.getId()))
                                .expectSubscription()
                                .expectNext(updatedAuthor2));


    }


    @Test
    void testDelete() {
        authorRepository.delete(AUTHOR1.getId())
                .block();

        assertThat(mongoTemplate.findAll(Author.class)).doesNotContain(AUTHOR1);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        StepVerifier.create(authorRepository.delete(NO_SUCH_ID))
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("existsParameters")
    void exists(String authorId, boolean expected) {
        StepVerifier.create(authorRepository.exists(authorId))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> existsParameters() {
        return Stream.of(
                Arguments.of(AUTHOR2.getId(), true),
                Arguments.of(NO_SUCH_ID, false)
        );
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            authorRepository.save(new Author(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland())).block();
            authorRepository.save(new Author(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland())).block();
        }).isInstanceOf(DataIntegrityViolationException.class);

//        StepVerifier.create(Flux.just(NEW_AUTHOR, NEW_AUTHOR)
//                .flatMap(authorRepository::save)
//                .skip(1)
////                .doOnError(throwable -> {
////                    System.out.println("Exception thrown: "+throwable.toString());
////                })
//        )
//        .expectSubscription()
//        .expectError(DataIntegrityViolationException.class)
//        .verify();
    }
}