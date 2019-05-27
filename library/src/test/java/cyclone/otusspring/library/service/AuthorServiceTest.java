package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
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
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void create() {
        AuthorDto authorDtoToCreate = new AuthorDto(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());

        Author createdAuthor = authorService.save(authorDtoToCreate);

        assertThat(createdAuthor.getId()).isNotNull();
        assertThat(createdAuthor).isEqualToIgnoringGivenFields(authorDtoToCreate, "id");
        assertThat(authorService.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(createdAuthor);
    }

    @Test
    void testFindAll() {
        assertThat(authorService.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2, AUTHOR_WITHOUT_BOOKS);
    }

    @Test
    @DisplayName("deleting author with books throws exception")
    void deletingAuthorWithBooksThrowsException() {
        assertThatThrownBy(() -> {
            authorService.delete(AUTHOR1.getId());
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageStartingWith("Could not delete author");

    }

    @Test
    @DisplayName("deleting author without books does not throw exception")
    void deletingAuthorWithoutBooks() {
        authorService.delete(AUTHOR_WITHOUT_BOOKS.getId());
    }
}