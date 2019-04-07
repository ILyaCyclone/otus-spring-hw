package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
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
    void findAll() {
        List<Book> books = bookService.findAll();

        assertThat(books).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }
}