package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan("cyclone.otusspring.library.repository.jpa")
class CommentRepositoryJpaTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TestEntityManager tem;

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
    void testInsert() {
        long savedId = commentRepository.save(NEW_COMMENT).getCommentId();

        Comment actual = commentRepository.findOne(savedId);

        assertThat(actual.getCommentId()).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(NEW_COMMENT, "commentId");
    }

    @Test
    void testUpdate() {
        Comment updatedComment2 = new Comment(COMMENT2.getCommentId(), "Updated " + COMMENT2.getCommentator()
                , "Updated" + COMMENT2.getText(), COMMENT2.getDate(), COMMENT2.getBook());
        commentRepository.save(updatedComment2);

        Comment actual = commentRepository.findOne(updatedComment2.getCommentId());

        assertThat(actual).isEqualToIgnoringGivenFields(updatedComment2, "book");
        //TODO error Unable to obtain the value of the field <'$$_hibernate_interceptor'> on next assert
//        assertThat(actual.getBook()).isEqualToIgnoringGivenFields(updatedComment2.getBook(), "comments");


        Book actualBook = actual.getBook();
        Book updatedComment2Book = updatedComment2.getBook();

        // this workaround works
//        assertThat(actualBook)
//                .isEqualToIgnoringGivenFields(updatedComment2Book, "comments"
////            , "$$_hibernate_interceptor"
//        );

        assertThat(actualBook)
                .isEqualToComparingOnlyGivenFields(updatedComment2Book
                        , "bookId", "title", "year", "author", "genre");


    }

    @Test
    void delete() {
        Comment commentToDelete = tem.find(Comment.class, COMMENT1.getCommentId());
        commentRepository.delete(commentToDelete);

        assertThat(commentRepository.findByBook(BOOK1))
                .usingElementComparatorIgnoringFields("book")
                .hasSize(1)
                .doesNotContain(COMMENT1);
    }

    @Test
    void deleteById() {
        commentRepository.delete(COMMENT3.getCommentId());

        assertThat(commentRepository.findByBook(BOOK1))
                .usingElementComparatorIgnoringFields("book")
                .hasSize(1)
                .doesNotContain(COMMENT3);
    }
}