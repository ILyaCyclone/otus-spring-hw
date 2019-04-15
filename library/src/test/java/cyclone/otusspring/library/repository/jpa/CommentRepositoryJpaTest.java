package cyclone.otusspring.library.repository.jpa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.repository.jpa")
class CommentRepositoryJpaTest {

    @Autowired
    CommentRepositoryJpa commentRepository;

    @Test
    void findOne() {
        assertThat(commentRepository.findOne(COMMENT2.getCommentId()))
                .isEqualToIgnoringGivenFields(COMMENT2, "book");
    }

    @Test
    void findByBook() {
        assertThat(commentRepository.findByBook(BOOK1))
                .usingElementComparatorIgnoringFields("book")
                .containsExactly(COMMENT1, COMMENT3);
    }

    @Test
    void findByBookId() {
        assertThat(commentRepository.findByBookId(BOOK2.getBookId()))
                .usingElementComparatorIgnoringFields("book")
                .containsExactly(COMMENT2, COMMENT4);
    }

    @Test
    void findAllCommentator() {
        assertThat(commentRepository.findByCommentator(COMMENT1.getCommentator()))
                .usingElementComparatorIgnoringFields("book")
                .containsExactly(COMMENT1, COMMENT2);
    }

    @Test
    @Disabled("not implemented yet")
    void save() {
    }

    @Test
    @Disabled("not implemented yet")
    void delete() {
    }

    @Test
    @Disabled("not implemented yet")
    void deleteById() {
    }
}