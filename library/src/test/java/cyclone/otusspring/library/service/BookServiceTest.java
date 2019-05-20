package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void create() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor().getId(), NEW_BOOK.getGenre().getId());

        Book createdBook = bookService.create(bookDtoToCreate);

        assertThat(createdBook.getId()).isNotNull();
        assertAll(() -> assertThat(createdBook.getTitle()).isEqualTo(bookDtoToCreate.getTitle())
                , () -> assertThat(createdBook.getYear()).isEqualTo(bookDtoToCreate.getYear()));
        assertThat(bookService.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(createdBook);
    }

    @Test
    @DisplayName("creating a book with non existent author fails")
    void create_fail_nonExistentAuthor() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NO_SUCH_ID, NEW_BOOK.getGenre().getId());

        assertThatThrownBy(() -> bookService.create(bookDtoToCreate))
                .hasMessage("Could not create book")
                .hasCause(new NotFoundException("Author ID " + NO_SUCH_ID + " not found"));
    }

    @Test
    @DisplayName("creating a book with non existent genre fails")
    void create_fail_nonExistentGenre() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor().getId(), NO_SUCH_ID);

        assertThatThrownBy(() -> bookService.create(bookDtoToCreate))
                .hasMessage("Could not create book")
                .hasCause(new NotFoundException("Genre ID " + NO_SUCH_ID + " not found"));
    }

    @Test
    void findAll() {
        List<Book> books = bookService.findAll();

        assertThat(books).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("comments")
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }
}