package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void create() {
        final Book bookToCreate = new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre());

        StepVerifier.create(bookService.save(bookToCreate))
                .expectSubscription()
                .assertNext(book -> {
                    assertThat(book.getId()).isNotNull();
                    assertThat(book).isEqualToIgnoringGivenFields(bookToCreate, "id", "comments");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("possible to use BookWithoutComments to create book")
    void create_withoutComments() {
        final BookWithoutComments bookToCreate = new BookWithoutComments(null, NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre());

        BookWithoutComments createdBook = bookService.save(bookToCreate)
                .block();

        assertThat(createdBook.getId()).isNotNull();
        assertThat(createdBook).isEqualToIgnoringGivenFields(bookToCreate, "id", "comments");
        //TODO unblock
        assertThat(bookService.findOne(createdBook.getId()).block()).isEqualToIgnoringGivenFields(bookToCreate, "comments");


    }

    @Test
    @DisplayName("updating book without comments doesn't erase existing comments")
    void update_withoutComments() {
        final BookWithoutComments bookToCreate = new BookWithoutComments(BOOK1.getId()
                , "upd " + BOOK1.getTitle(), 1 + BOOK1.getYear(), BOOK2.getAuthor(), BOOK2.getGenre());

        BookWithoutComments createdBook = bookService.save(bookToCreate)
                .block();

        assertThat(createdBook.getId()).isNotNull();
        assertThat(createdBook).isEqualToIgnoringGivenFields(bookToCreate, "id", "comments");

        StepVerifier.create(bookService.findOne(createdBook.getId()))
                .assertNext(book -> {
                    assertThat(book).isEqualToIgnoringGivenFields(bookToCreate, "comments");
                    assertThat(book.getComments()).containsExactly(COMMENT1, COMMENT3);
                });
    }

    @Test
    @DisplayName("creating a book with non existent author fails")
    void create_fail_nonExistentAuthor() {
        Book bookToCreate = new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), new Author(NO_SUCH_ID), NEW_BOOK.getGenre());

        assertThatThrownBy(() -> bookService.save(bookToCreate).block())
                .hasMessage("Could not save book")
                .hasCause(new NotFoundException("Author ID " + NO_SUCH_ID + " not found"));
    }

    @Test
    @DisplayName("creating a book with non existent genre fails")
    void create_fail_nonExistentGenre() {
        Book bookToCreate = new Book(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), new Genre(NO_SUCH_ID, "no such genre"));

        assertThatThrownBy(() -> bookService.save(bookToCreate).block())
                .hasMessage("Could not save book")
                .hasCause(new NotFoundException("Genre ID " + NO_SUCH_ID + " not found"));
    }

    @Test
    void findAll() {
        List<Book> books = bookService.findAll().collectList().block();

        assertThat(books).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("comments")
                .containsExactly(BOOK5, BOOK2, BOOK4, BOOK3, BOOK1);


    }

    @Test
    void cascadeSaveAuthor() {
        Book book = bookService.findOne(BOOK1.getId()).block();
        Author author = book.getAuthor();
        final String updatedFirstname = "Cascade save " + author.getFirstname();
        author.setFirstname(updatedFirstname);
        book.setAuthor(author);

        bookService.save(book).block();

        Book bookAfterSave = bookService.findOne(BOOK1.getId()).block();
        assertThat(bookAfterSave.getAuthor().getFirstname())
                .as("Author should be updated by cascade")
                .isEqualTo(updatedFirstname);
    }


    @Test
    void cascadeSaveGenre() {
        Book book = bookService.findOne(BOOK1.getId()).block();
        Genre genre = book.getGenre();
        final String updatedName = "Cascade save " + genre.getName();
        genre.setName(updatedName);
        book.setGenre(genre);

        bookService.save(book).block();

        Book bookAfterSave = bookService.findOne(BOOK1.getId()).block();
        assertThat(bookAfterSave.getGenre().getName())
                .as("Genre should be updated by cascade")
                .isEqualTo(updatedName);
    }
}