package cyclone.otusspring.library.dao.jpa;

import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Book;
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
class BookDaoJpaTest {

    @Autowired
    BookDao bookDao;

    @Autowired
    TestEntityManager tem;

    @Test
    void findAll() {
        assertThat(bookDao.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }

    @ParameterizedTest
    @MethodSource("findByTitleParameters")
    void findByTitle(String title, Book[] expected) {
        assertThat(bookDao.findByTitle(title)).containsExactly(expected);
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
        assertThatThrownBy(() -> bookDao.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void findOne() {
        assertThat(bookDao.findOne(2)).isEqualTo(BOOK2);
    }



    @Test
    void testInsert() {
        long savedId = bookDao.save(NEW_BOOK).getBookId();

        Book actual = bookDao.findOne(savedId);

        assertThat(actual.getBookId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_BOOK, "bookId");
    }

    @Test
    void testUpdate() {
        Book bookToUpdate = tem.find(Book.class, BOOK2.getBookId());

        bookToUpdate.setTitle("updated " + bookToUpdate.getTitle());
        bookToUpdate.setYear(bookToUpdate.getYear() + 1);

        Book updatedBook = bookDao.save(bookToUpdate);

        assertThat(updatedBook).isEqualToComparingFieldByField(bookToUpdate);
    }

    @Test
    void testDelete() {
        Book bookToDelete = tem.find(Book.class, BOOK2.getBookId());

        bookDao.delete(bookToDelete);
        assertThat(bookDao.findAll()).containsExactly(BOOK5, BOOK4, BOOK3, BOOK1);
    }

    @Test
    void testDeleteById() {
        bookDao.delete(BOOK1.getBookId());
        assertThat(bookDao.findAll()).containsExactly(BOOK5, BOOK2, BOOK4, BOOK3);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> bookDao.delete(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testExistTrue() {
        assertThat(bookDao.exists(BOOK2.getBookId())).isTrue();
    }

    @Test
    void testExistFalse() {
        assertThat(bookDao.exists(NO_SUCH_ID)).isFalse();
    }
}