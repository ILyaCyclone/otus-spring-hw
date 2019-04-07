package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDetails;
import cyclone.otusspring.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;


    @Test
    void createBook() {
        Book createdBook = bookService.createBook(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthorId(), NEW_BOOK.getGenreId());

        assertThat(createdBook).isEqualToIgnoringGivenFields(NEW_BOOK, "bookId");
        assertThat(createdBook.getBookId()).isNotNull();
        assertThat(bookService.findAll()).containsExactly(createdBook, BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);
    }

    @Test
    void findAllWithDetails() {
        List<BookDetails> booksWithDetails = bookService.findAllWithDetails();

        assertThat(booksWithDetails).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new BookDetails(BOOK5.getBookId(), BOOK5.getTitle(), BOOK5.getYear(), AUTHOR3, GENRE3)
                        , new BookDetails(BOOK2.getBookId(), BOOK2.getTitle(), BOOK2.getYear(), AUTHOR1, GENRE1)
                        , new BookDetails(BOOK4.getBookId(), BOOK4.getTitle(), BOOK4.getYear(), AUTHOR2, GENRE2)
                        , new BookDetails(BOOK3.getBookId(), BOOK3.getTitle(), BOOK3.getYear(), AUTHOR2, GENRE2)
                        , new BookDetails(BOOK1.getBookId(), BOOK1.getTitle(), BOOK1.getYear(), AUTHOR1, GENRE1)
                );
    }
}