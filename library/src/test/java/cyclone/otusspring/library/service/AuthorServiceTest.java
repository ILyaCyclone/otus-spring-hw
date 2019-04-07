package cyclone.otusspring.library.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void testFindAll() {
        assertThat(authorService.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2);

    }

}