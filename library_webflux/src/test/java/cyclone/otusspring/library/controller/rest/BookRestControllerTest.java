package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.rest.BookRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    BookService bookService;

    @MockBean
    AuthorService authorService; // needed in bookMapper

    @MockBean
    GenreService genreService; // needed in bookMapper

    @MockBean
    BookRepository bookRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookMapper bookMapper;


    @Test
    void findAll() {
        when(bookService.findAll()).thenReturn(Flux.just(BOOK1, BOOK2));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(BookListElementDto.class)
                .contains(bookMapper.toBooksElementDto(BOOK1), bookMapper.toBooksElementDto(BOOK2));

        verify(bookService).findAll();
    }

    @Test
    void findOne() {
        when(bookService.findOne("1")).thenReturn(Mono.just(BOOK1));

        EntityExchangeResult<BookDto> exchangeResult = webTestClient.get().uri(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BookDto.class)
                .returnResult();

        verify(bookService).findOne("1");

        BookDto actualBookDto = exchangeResult.getResponseBody();
        assertThat(actualBookDto)
                .isEqualToComparingOnlyGivenFields(BOOK1, "title", "year");
        assertThat(actualBookDto.getAuthorId()).isEqualTo(BOOK1.getAuthor().getId());
        assertThat(actualBookDto.getGenreId()).isEqualTo(BOOK1.getGenre().getId());
    }

    @Test
    void create() {
        when(authorService.findOne("1")).thenReturn(Mono.just(AUTHOR1));
        when(genreService.findOne("1")).thenReturn(Mono.just(GENRE1));

        final BookDto bookDtoToCreate = new BookDto("new title", 2000, "1", "1");
        Book bookToCreate = bookMapper.toBook(bookDtoToCreate);
        Book createdBook = bookMapper.toBook(bookDtoToCreate);
        createdBook.setId("generated-new-id");

        when(bookService.save(bookMapper.toBook(bookDtoToCreate))).thenReturn(Mono.just(createdBook));

        EntityExchangeResult<BookDto> exchangeResult = webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(bookDtoToCreate), BookDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BookDto.class)
                .returnResult();

        verify(bookService).save(bookToCreate);

        BookDto createdBookDto = exchangeResult.getResponseBody();

        assertThat(createdBookDto.getId()).isNotNull();
        assertThat(createdBookDto).isEqualToIgnoringGivenFields(bookDtoToCreate, "id");
    }

    @Test
    void update() {
        when(authorService.findOne("1")).thenReturn(Mono.just(AUTHOR1));
        when(genreService.findOne("1")).thenReturn(Mono.just(GENRE1));

        final BookDto bookDtoToUpdate = new BookDto("1", "upd title", 2000, "1", "1");
        BookWithoutComments bookToUpdate = bookMapper.toBookWithoutComments(bookDtoToUpdate);
        when(bookService.save(bookToUpdate)).thenReturn(Mono.just(bookToUpdate));

        webTestClient.put().uri(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(bookDtoToUpdate), BookDto.class)
                .exchange()
                .expectStatus().isNoContent();

        verify(bookService).save(bookToUpdate);
    }

    @Test
    void delete() throws Exception {
        webTestClient.delete().uri(BASE_URL + "/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(bookService).delete("1");
    }

    @Test
    void saveComment() {
        when(bookService.findOne("1")).thenReturn(Mono.just(BOOK1));
        when(bookService.save(BOOK1)).thenReturn(Mono.just(BOOK1));
        CommentDto commentDtoToCreate = new CommentDto("1", "new commentator", "new text");

        webTestClient.post().uri(BASE_URL + "/1/comments/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDtoToCreate), CommentDto.class)
                .exchange()
                .expectStatus().isCreated();

        verify(bookService).findOne("1");
        verify(bookService).save((Book) any());
    }
}
