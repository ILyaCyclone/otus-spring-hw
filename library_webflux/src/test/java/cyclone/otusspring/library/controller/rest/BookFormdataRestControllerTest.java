package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.BookFormDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.GenreMapper;
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
import static cyclone.otusspring.library.controller.rest.BookFormdataRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookFormdataRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    BookService bookService;

    @MockBean
    AuthorService authorService; // needed in bookMapper

    @MockBean
    GenreService genreService; // needed in bookMapper

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    AuthorMapper authorMapper;

    @Autowired
    GenreMapper genreMapper;



    @Test
    void getBookFormData() {
        when(bookService.findOne("1")).thenReturn(Mono.just(BOOK1));
        when(authorService.findAll()).thenReturn(Flux.just(AUTHOR1, AUTHOR2));
        when(genreService.findAll()).thenReturn(Flux.just(GENRE1, GENRE2));

        EntityExchangeResult<BookFormDto> exchangeResult = webTestClient.get().uri(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BookFormDto.class)
                .returnResult();

        verify(bookService).findOne("1");

        BookFormDto bookFormDto = exchangeResult.getResponseBody();
        assertThat(bookFormDto.getBookDto())
                .isEqualTo(bookMapper.toBookDto(BOOK1));
        assertThat(bookFormDto.getAllAuthors())
                .containsExactly(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR2));
        assertThat(bookFormDto.getAllGenres())
                .containsExactly(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE2));
    }

    @Test
    void getNewBookFormData() {
        when(authorService.findAll()).thenReturn(Flux.just(AUTHOR1, AUTHOR2));
        when(genreService.findAll()).thenReturn(Flux.just(GENRE1, GENRE2));

        EntityExchangeResult<BookFormDto> exchangeResult = webTestClient.get().uri(BASE_URL + "/new")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BookFormDto.class)
                .returnResult();

        verify(bookService, never()).findOne(any());

        BookFormDto bookFormDto = exchangeResult.getResponseBody();
        assertThat(bookFormDto.getBookDto()).isNull();
        assertThat(bookFormDto.getAllAuthors())
                .containsExactly(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR2));
        assertThat(bookFormDto.getAllGenres())
                .containsExactly(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE2));
    }
}