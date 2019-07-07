package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.test.StepVerifier;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    void create() {
        Genre genreToCreate = new Genre(NEW_GENRE.getName());

        StepVerifier.create(genreService.save(genreToCreate))
                .expectSubscription()
                .assertNext(author -> {
                    assertThat(author.getId()).isNotNull();
                    assertThat(author).isEqualToIgnoringGivenFields(genreToCreate, "id");
                })
                .verifyComplete();
    }

    @Test
    void findAll() {
        StepVerifier.create(genreService.findAll())
                .expectSubscription()
                .expectNext(GENRE1, GENRE4, GENRE3, GENRE2, GENRE_WITHOUT_BOOKS)
                .verifyComplete();
    }

    @Test
    void findOne() {
        StepVerifier.create(genreService.findOne("2"))
                .expectSubscription()
                .expectNext(GENRE2)
                .verifyComplete();
    }

    @Test
    @DisplayName("deleting genre with books throws exception")
    void deletingAuthorWithBooksThrowsException() {
        StepVerifier.create(genreService.delete(GENRE1.getId()))
                .expectSubscription()
                .expectError(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("deleting genre without books does not throw exception")
    void deletingAuthorWithoutBooks() {
        genreService.delete(GENRE_WITHOUT_BOOKS.getId())
                .doOnError(throwable -> Assertions.fail("no exception expected"));
    }
}