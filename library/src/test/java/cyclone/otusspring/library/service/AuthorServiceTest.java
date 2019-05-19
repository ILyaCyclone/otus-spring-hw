package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.MongoTestState;
import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@Transactional
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    //    @Autowired
//    MongoTemplate mongoTemplate;

    @Autowired
    MongoTestState mongoTestState;

    @BeforeEach
    void reInitDB() {
        mongoTestState.resetState();
    }

    @Test
    void create() {
        AuthorDto authorDtoToCreate = new AuthorDto(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());

        Author createdAuthor = authorService.create(authorDtoToCreate);

        assertThat(createdAuthor).isEqualToIgnoringGivenFields(authorDtoToCreate, "id");
        assertThat(authorService.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(createdAuthor);
    }

    @Test
    void testFindAll() {
        assertThat(authorService.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2);
    }
}