package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(ResetStateExtension.class)
//@Transactional
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