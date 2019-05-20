package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    void create() {
        Genre genreToCreate = new Genre(NEW_GENRE.getName());

        Genre createdGenre = genreService.create(genreToCreate.getName());

        assertThat(createdGenre).isEqualToIgnoringGivenFields(genreToCreate, "id");
        assertThat(genreService.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(createdGenre);
    }

    @Test
    void testFindAll() {
        assertThat(genreService.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2, GENRE_WITHOUT_BOOKS);
    }

    @Test
    @DisplayName("deleting genre with books throws exception")
    void deletingAuthorWithBooksThrowsException() {
        assertThatThrownBy(() -> {
            genreService.delete(GENRE1.getId());
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageStartingWith("Could not delete genre");

    }

    @Test
    @DisplayName("deleting genre without books does not throw exception")
    void deletingAuthorWithoutBooks() {
        genreService.delete(GENRE_WITHOUT_BOOKS.getId());
    }
}