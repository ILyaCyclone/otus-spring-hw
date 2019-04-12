package cyclone.otusspring.library.dao.jpa;

import cyclone.otusspring.library.dao.AuthorDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.TestData.NO_SUCH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.dao.jpa")
@ActiveProfiles(DataAccessProfiles.JPA)
class AuthorDaoJpaTest {

    @Autowired
    AuthorDao authorDao;

    @Autowired
    TestEntityManager tem;


    @Test
    void findAll() {
        assertThat(authorDao.findAll()).containsExactly(AUTHOR1, AUTHOR3, AUTHOR2); // 1, 3, 2 because of ordering
    }

    @ParameterizedTest
    @MethodSource("findByNameParameters")
    void findByName(String nameQuery, Author[] expected) {
        assertThat(authorDao.findByName(nameQuery)).containsExactly(expected);
    }

    private static Stream<Arguments> findByNameParameters() {
        return Stream.of(
                Arguments.of("gabri", new Author[]{AUTHOR3}),
                Arguments.of("ar", new Author[]{AUTHOR1, AUTHOR3})
        );
    }

    @Test
    void findOne() {
        assertThat(authorDao.findOne(2)).isEqualTo(AUTHOR2);
    }

    @Test
    @DisplayName("finding non existent ID throws exception")
    void findOne_nonExistent() {
        assertThatThrownBy(() -> authorDao.findOne(NO_SUCH_ID)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }



    @Test
    void testInsert() {
        long savedId = authorDao.save(NEW_AUTHOR).getAuthorId();

        Author actual = authorDao.findOne(savedId);

        assertThat(actual.getAuthorId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_AUTHOR, "authorId");
    }

    @Test
    void testUpdate() {
        Author updatedAuthor2 = new Author(AUTHOR2.getAuthorId(), "Updated " + AUTHOR2.getFirstname(), "Updated " + AUTHOR2.getLastname(), "Updated " + AUTHOR2.getHomeland());
        authorDao.save(updatedAuthor2);

        Author actual = authorDao.findOne(updatedAuthor2.getAuthorId());

        assertThat(actual).isEqualToComparingFieldByField(updatedAuthor2);
    }

    @Test
    void testDelete() {
        Author bookToDelete = tem.find(Author.class, AUTHOR2.getAuthorId());

        authorDao.delete(bookToDelete);
        assertThat(authorDao.findAll()).containsExactly(AUTHOR1, AUTHOR3);
    }

    @Test
    void testDeleteById() {
        authorDao.delete(AUTHOR1.getAuthorId());
        assertThat(authorDao.findAll()).containsExactly(AUTHOR3, AUTHOR2);
    }

    @Test
    @DisplayName("deleting non existent ID throws exception")
    void testDeleteNonExistent() {
        assertThatThrownBy(() -> authorDao.delete(NO_SUCH_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testExistTrue() {
        assertThat(authorDao.exists(AUTHOR2.getAuthorId())).isTrue();
    }

    @Test
    void testExistFalse() {
        assertThat(authorDao.exists(NO_SUCH_ID)).isFalse();
    }
}