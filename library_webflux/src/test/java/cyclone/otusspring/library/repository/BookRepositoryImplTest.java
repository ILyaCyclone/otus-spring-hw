package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Book;
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
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@ComponentScan("cyclone.otusspring.library.repository")
@ExtendWith(ResetStateExtension.class)
class BookRepositoryImplTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAll() {
        StepVerifier.create(bookRepository.findAll())
                .expectSubscription()
                .expectNext(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1)
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("findByTitleParameters")
    void findByTitle(String title, Book[] expected) {
        StepVerifier.create(bookRepository.findByTitle(title))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> findByTitleParameters() {
        return Stream.of(
                Arguments.of("air", new Book[]{BOOK2}),
                Arguments.of("it", new Book[]{BOOK5, BOOK3})
        );
    }

    @Test
    void findOne() {
        StepVerifier.create(bookRepository.findOne("2"))
                .expectSubscription()
                .assertNext(book -> assertThat(book).isEqualToIgnoringGivenFields(BOOK2, "comments"))
                .verifyComplete();
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        StepVerifier.create(bookRepository.findOne(NO_SUCH_ID))
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void testInsert() {
        bookRepository.save(NEW_BOOK)
                .map(Book::getId)
                .subscribe(savedId ->
                        StepVerifier.create(bookRepository.findOne(savedId))
                                .expectSubscription()
                                .assertNext(actual -> {
                                    assertThat(actual.getId()).isNotNull();
                                    assertThat(actual).isEqualToIgnoringGivenFields(NEW_BOOK, "id");
                                })
                                .verifyComplete()
                );
    }

    @Test
    void testUpdate() {
        Book bookToUpdate = mongoTemplate.findById(BOOK2.getId(), Book.class);

        bookToUpdate.setTitle("updated " + bookToUpdate.getTitle());
        bookToUpdate.setYear(bookToUpdate.getYear() + 1);

        bookRepository.save(bookToUpdate)
                .subscribe(savedBook ->
                        StepVerifier.create(bookRepository.findOne(BOOK2.getId()))
                                .expectSubscription()
                                .expectNext(bookToUpdate));
    }

    @Test
    void testDelete() {
        bookRepository.delete(BOOK2.getId()).block();

        assertThat(mongoTemplate.findAll(Book.class))
                .usingElementComparatorIgnoringFields("comments")
                .doesNotContain(BOOK2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        StepVerifier.create(bookRepository.delete(NO_SUCH_ID))
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("existsParameters")
    void exists(String bookId, boolean expected) {
        StepVerifier.create(bookRepository.exists(bookId))
                .expectSubscription()
                .expectNext(expected)
                .verifyComplete();
    }

    private static Stream<Arguments> existsParameters() {
        return Stream.of(
                Arguments.of(BOOK2.getId(), true),
                Arguments.of(NO_SUCH_ID, false)
        );
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            bookRepository.save(new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre())).block();
            bookRepository.save(new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre())).block();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}