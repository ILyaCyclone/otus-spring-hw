package cyclone.otusspring.library.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    void testFindAll() {
        assertThat(genreService.findAll()).containsExactly(GENRE1, GENRE4, GENRE3, GENRE2);
    }
}