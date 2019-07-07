package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.service.AuthorService;
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
import static cyclone.otusspring.library.controller.rest.AuthorRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    AuthorService authorService;

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthorMapper authorMapper;

    @Test
    void findAll() {
        when(authorService.findAll()).thenReturn(Flux.just(AUTHOR1, AUTHOR3, AUTHOR2));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(AuthorDto.class)
                .contains(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR3), authorMapper.toAuthorDto(AUTHOR2));
    }

    @Test
    void findOne() {
        when(authorService.findOne("1")).thenReturn(Mono.just(AUTHOR1));

        webTestClient.get().uri(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AuthorDto.class)
                .isEqualTo(authorMapper.toAuthorDto(AUTHOR1));
    }

    @Test
    void create() {
        final AuthorDto authorDtoToCreate = new AuthorDto("new firstname", "new lastname", "new homeland");
        Author createdAuthor = authorMapper.toAuthor(authorDtoToCreate);
        createdAuthor.setId("generated-new-id");

        when(authorService.save(authorMapper.toAuthor(authorDtoToCreate))).thenReturn(Mono.just(createdAuthor));

        EntityExchangeResult<AuthorDto> exchangeResult = webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authorDtoToCreate), AuthorDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AuthorDto.class)
                .returnResult();

        AuthorDto createdAuthorDto = exchangeResult.getResponseBody();
        assertThat(createdAuthorDto.getId()).isNotNull();
        assertThat(createdAuthorDto).isEqualToIgnoringGivenFields(authorDtoToCreate, "id");
    }

    @Test
    void update() {
        final AuthorDto authorDtoToUpdate = new AuthorDto("1", "upd fistname", "upd lastname", "upd homeland");
        Author authorToUpdate = authorMapper.toAuthor(authorDtoToUpdate);

        webTestClient.put().uri(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authorDtoToUpdate), AuthorDto.class)
                .exchange()
                .expectStatus().isNoContent();

        verify(authorService).save(authorToUpdate);
    }

    @Test
    void delete() {
        webTestClient.delete().uri(BASE_URL + "/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(authorService).delete("1");
    }
}
