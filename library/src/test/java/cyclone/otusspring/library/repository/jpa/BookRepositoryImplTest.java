package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.repository")
class BookRepositoryImplTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TestEntityManager tem;

    @Test
    void findAll() {
        assertThat(bookRepository.findAll()).usingElementComparatorIgnoringFields("comments")
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }

    @ParameterizedTest
    @MethodSource("findByTitleParameters")
    void findByTitle(String title, Book[] expected) {
        assertThat(bookRepository.findByTitle(title))
                .usingElementComparatorIgnoringFields("comments")
                .containsExactly(expected);
    }

    private static Stream<Arguments> findByTitleParameters() {
        return Stream.of(
                Arguments.of("air", new Book[]{BOOK2}),
                Arguments.of("it", new Book[]{BOOK5, BOOK3})
        );
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> bookRepository.findOne(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findOne() {
        assertThat(bookRepository.findOne("2")).isEqualToIgnoringGivenFields(BOOK2, "comments");
    }



    @Test
    void testInsert() {
        String savedId = bookRepository.save(NEW_BOOK).getId();

        Book actual = bookRepository.findOne(savedId);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_BOOK, "id");
    }

    @Test
    void testUpdate() {
        Book bookToUpdate = tem.find(Book.class, BOOK2.getId());

        bookToUpdate.setTitle("updated " + bookToUpdate.getTitle());
        bookToUpdate.setYear(bookToUpdate.getYear() + 1);

        Book updatedBook = bookRepository.save(bookToUpdate);
        tem.flush();

        assertThat(updatedBook).isEqualToComparingFieldByField(bookToUpdate);
    }

    @Test
    void testDelete() {
        Book bookToDelete = tem.find(Book.class, BOOK2.getId());

        bookRepository.delete(bookToDelete);
        assertThat(bookRepository.findAll())
                .usingElementComparatorIgnoringFields("comments")
                .doesNotContain(bookToDelete);
    }

    @Test
    void testDeleteById() {
        bookRepository.delete(BOOK1.getId());
        assertThat(bookRepository.findAll())
                .usingElementComparatorIgnoringFields("comments")
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> bookRepository.delete(NO_SUCH_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void testExistsTrue() {
        assertThat(bookRepository.exists(BOOK2.getId())).isTrue();
    }

    @Test
    void testExistsFalse() {
        assertThat(bookRepository.exists(NO_SUCH_ID)).isFalse();
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            bookRepository.save(new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre()));
            bookRepository.save(new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre()));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}