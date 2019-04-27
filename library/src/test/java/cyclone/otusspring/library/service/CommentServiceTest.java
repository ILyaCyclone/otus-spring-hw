package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;


    @Test
    void findByBookId() {
        List<Comment> comments = commentService.findByBookId(BOOK1.getBookId());

        assertThat(comments).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("book")
                .containsExactly(COMMENT1, COMMENT3);
    }

    @Test
    void create() {
        final long bookId = BOOK1.getBookId();
        final String username = "username";
        final String text = "comment text";

        CommentDto commentDtoToCreate = new CommentDto(bookId, username, text);

        // act
        commentService.create(commentDtoToCreate);

        // find saved comment for assertions
        List<Comment> commentsByUsernameAndText = commentService.findByBookId(bookId).stream()
                .filter(comment -> comment.getCommentator().equals(username))
                .filter(comment -> comment.getText().equals(text))
                .collect(Collectors.toList());

        assertThat(commentsByUsernameAndText).as("only 1 comment saved")
                .hasSize(1);

        Comment createdComment = commentsByUsernameAndText.get(0);

        assertThat(createdComment.getCommentId()).isNotNull();
        assertThat(createdComment.getBook().getBookId()).isEqualTo(bookId);
        assertThat(createdComment.getDate()).isNotNull();
        assertThat(createdComment).isEqualToIgnoringGivenFields(commentDtoToCreate, "commentId", "date", "book");
    }

    @Test
    void delete() {
        commentService.delete(COMMENT4.getCommentId());

        assertThat(commentService.findByBookId(BOOK2.getBookId()))
                .hasSize(1)
                .doesNotContain(COMMENT4);
    }
}