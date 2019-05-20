package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@ComponentScan("cyclone.otusspring.library.repository")
@ExtendWith(ResetStateExtension.class)
class AuthorRepositoryImplTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAll() {
        assertThat(authorRepository.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2, AUTHOR_WITHOUT_BOOKS); // 1, 3, 2, wo_books because of ordering and case
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Author[] expected) {
        assertThat(authorRepository.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("GabrI", new Author[]{AUTHOR3}),
                Arguments.of("aR", new Author[]{AUTHOR1, AUTHOR3})
        );
    }

    @Test
    void findOne() {
        assertThat(authorRepository.findOne("2")).isEqualTo(AUTHOR2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> authorRepository.findOne(NO_SUCH_ID)).isInstanceOf(NotFoundException.class);
    }



    @Test
//    @DirtiesContext
    void testInsert() {
        String savedId = authorRepository.save(NEW_AUTHOR).getId();

        Author actual = authorRepository.findOne(savedId);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_AUTHOR, "id");
    }

    @Test
//    @DirtiesContext
    void testUpdate() {
        Author updatedAuthor2 = new Author(AUTHOR2.getId(), "Updated " + AUTHOR2.getFirstname(), "Updated " + AUTHOR2.getLastname(), "Updated " + AUTHOR2.getHomeland());
        authorRepository.save(updatedAuthor2);
//        tem.flush(); // send update to database

        Author actual = authorRepository.findOne(updatedAuthor2.getId());

        assertThat(actual).isEqualToComparingFieldByField(updatedAuthor2);
    }

    @Test
//    @DirtiesContext
    void testDelete() {
        Author bookToDelete = mongoTemplate.findById(AUTHOR2.getId(), Author.class);

        authorRepository.delete(bookToDelete);
        assertThat(authorRepository.findAll()).doesNotContain(AUTHOR2);
    }

    @Test
//    @DirtiesContext
    void testDeleteById() {
        authorRepository.delete(AUTHOR1.getId());
        assertThat(authorRepository.findAll()).doesNotContain(AUTHOR1);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> authorRepository.delete(NO_SUCH_ID)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testExistsTrue() {
        assertThat(authorRepository.exists(AUTHOR2.getId())).isTrue();
    }

    @Test
    void testExistsFalse() {
        assertThat(authorRepository.exists(NO_SUCH_ID)).isFalse();
    }

    @Test
    @DisplayName("adding non unique records throws exception")
    void uniqueViolationThrowsException() {
        assertThatThrownBy(() -> {
            authorRepository.save(new Author(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland()));
            authorRepository.save(new Author(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland()));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}