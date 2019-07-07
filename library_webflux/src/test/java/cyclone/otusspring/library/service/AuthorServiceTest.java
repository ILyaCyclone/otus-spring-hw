package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.test.StepVerifier;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void create() {
        final Author authorToCreate = new Author(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());
        StepVerifier.create(authorService.save(authorToCreate))
                .expectSubscription()
                .assertNext(author -> {
                    assertThat(author.getId()).isNotNull();
                    assertThat(author).isEqualToIgnoringGivenFields(authorToCreate, "id");
                });

//        assertThat(mongoTemplate.findAll(Author.class))
//                .usingElementComparatorIgnoringFields("id")
//                .contains(authorToCreate);
    }

    @Test
    void findAll() {
        StepVerifier.create(authorService.findAll())
                .expectSubscription()
                .expectNext(AUTHOR1, AUTHOR3, AUTHOR2, AUTHOR_WITHOUT_BOOKS)
                .verifyComplete();
    }

    @Test
    void findOne() {
        StepVerifier.create(authorService.findOne("1"))
                .expectSubscription()
                .expectNext(AUTHOR1)
                .verifyComplete();
    }



    @Test
    @DisplayName("deleting author with books throws exception")
    void deletingAuthorWithBooksThrowsException() {
        StepVerifier.create(authorService.delete(AUTHOR1.getId()))
                .expectSubscription()
                .expectError(DataIntegrityViolationException.class);
//        assertThatThrownBy(() -> authorService.delete(AUTHOR1.getId()).block())
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageStartingWith("Could not delete author");

    }

    @Test
    @DisplayName("deleting author without books does not throw exception")
    void deletingAuthorWithoutBooks() {
        authorService.delete(AUTHOR_WITHOUT_BOOKS.getId());
    }
}