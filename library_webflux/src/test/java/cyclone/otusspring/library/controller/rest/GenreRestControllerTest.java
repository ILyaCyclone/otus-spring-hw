package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
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
import static cyclone.otusspring.library.controller.rest.GenreRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    GenreService genreService;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GenreMapper genreMapper;


    @Test
    void findAll() throws Exception {
        when(genreService.findAll()).thenReturn(Flux.just(GENRE1, GENRE3, GENRE2));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(GenreDto.class)
                .contains(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE3), genreMapper.toGenreDto(GENRE2));

        verify(genreService).findAll();
    }

    @Test
    void findOne() {
        when(genreService.findOne("1")).thenReturn(Mono.just(GENRE1));

        webTestClient.get().uri(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(GenreDto.class)
                .isEqualTo(genreMapper.toGenreDto(GENRE1));
    }

    @Test
    void create() throws Exception {
        final GenreDto genreDtoToCreate = new GenreDto("new name");
        Genre createdGenre = genreMapper.toGenre(genreDtoToCreate);
        createdGenre.setId("generated-new-id");

        when(genreService.save(genreMapper.toGenre(genreDtoToCreate))).thenReturn(Mono.just(createdGenre));

        EntityExchangeResult<GenreDto> exchangeResult = webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(genreDtoToCreate), GenreDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(GenreDto.class)
                .returnResult();

        GenreDto createdGenreDto = exchangeResult.getResponseBody();
        assertThat(createdGenreDto.getId()).isNotNull();
        assertThat(createdGenreDto).isEqualToIgnoringGivenFields(genreDtoToCreate, "id");
    }

    @Test
    void update() throws Exception {
        final GenreDto genreDtoToUpdate = new GenreDto("1", "upd name");
        Genre genreToUpdate = genreMapper.toGenre(genreDtoToUpdate);

        webTestClient.put().uri(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(genreDtoToUpdate), GenreDto.class)
                .exchange()
                .expectStatus().isNoContent();

        verify(genreService).save(genreToUpdate);
    }

    @Test
    void delete() throws Exception {
        webTestClient.delete().uri(BASE_URL + "/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(genreService).delete("1");
    }
}