package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ServiceTestConfiguration.class})
@ExtendWith(ResetStateExtension.class)
//@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;


    @Test
    void findByBookId() {
        List<Comment> comments = commentService.findByBookId(BOOK1.getId());

        assertThat(comments).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("book")
                .containsExactly(COMMENT1, COMMENT3);
    }

    @Test
    void create() {
        final String bookId = BOOK1.getId();
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

        assertThat(createdComment.getId()).isNotNull();
//        assertThat(createdComment.getBook().getId()).isEqualTo(bookId);
        assertThat(createdComment.getDate()).isNotNull();
        assertThat(createdComment).isEqualToIgnoringGivenFields(commentDtoToCreate, "id", "date");
    }

    @Test
    void delete() {
        commentService.delete(BOOK2.getId(), COMMENT4.getId());

        assertThat(commentService.findByBookId(BOOK2.getId()))
                .hasSize(1)
                .doesNotContain(COMMENT4);
    }
}