package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
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
        assertThat(genreService.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2);
    }
}