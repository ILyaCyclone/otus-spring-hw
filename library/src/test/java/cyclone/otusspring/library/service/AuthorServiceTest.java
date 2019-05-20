package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
//@Transactional
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    //    @Autowired
//    MongoTemplate mongoTemplate;

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