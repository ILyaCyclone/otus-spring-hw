package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@AutoConfigureTestDatabase
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void createBook() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor().getAuthorId(), NEW_BOOK.getGenre().getGenreId());

        Book createdBook = bookService.createBook(bookDtoToCreate);

        assertThat(createdBook.getBookId()).isNotNull();
        assertAll(() -> assertThat(createdBook.getTitle()).isEqualTo(bookDtoToCreate.getTitle())
                , () -> assertThat(createdBook.getYear()).isEqualTo(bookDtoToCreate.getYear()));
        assertThat(bookService.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(createdBook, BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }

    @Test
    @DisplayName("creating a book with non existent author fails")
    void createBook_fail_nonExistentAuthor() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NO_SUCH_ID, NEW_BOOK.getGenre().getGenreId());

        assertThatThrownBy(() -> bookService.createBook(bookDtoToCreate))
                .hasCauseInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith("Author")
                .hasMessageEndingWith("not found");
    }

    @Test
    @DisplayName("creating a book with non existent genre fails")
    void createBook_fail_nonExistentGenre() {
        BookDto bookDtoToCreate = new BookDto(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor().getAuthorId(), NO_SUCH_ID);

        assertThatThrownBy(() -> bookService.createBook(bookDtoToCreate))
                .hasCauseInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith("Genre")
                .hasMessageEndingWith("not found");
    }

    @Test
    void findAll() {
        List<Book> books = bookService.findAll();

        assertThat(books).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }
}