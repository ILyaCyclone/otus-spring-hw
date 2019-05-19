package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.CommentService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@Disabled("not yet implemented")

@SpringBootTest
//@AutoConfigureTestDatabase
@Transactional
class CommentCommandsTest {

    @Autowired
    CommentCommands commentCommands;

    @SpyBean
    CommentService commentService;
    @SpyBean
    CommentsFormatter commentsFormatter;


    @Test
    void signIn() {
        final String username = "someusername";

        String message = commentCommands.signIn(username);

        assertThat(message)
                .containsIgnoringCase("signed in")
                .contains(username);
        assertThat(commentCommands.getCurrentUser()).isEqualTo(username);
    }

    @Test
    void signOut() {
        String message = commentCommands.signOut();

        assertThat(message).containsIgnoringCase("signed out");
        assertThat(commentCommands.getCurrentUser()).isNull();
    }

    @Test
    void addComment() {
        final String commentText = "new comment";
        final String username = "somebody";

        CommentDto commentDto = new CommentDto(BOOK1.getId(), username, commentText);

        commentCommands.signIn(username);
        String message = commentCommands.addComment(BOOK1.getId(), commentText);

        assertThat(message).containsIgnoringCase("saved");
        verify(commentService).create(commentDto);
    }

    @ParameterizedTest
    @DisplayName("listComments (with --verbose and without)")
    @ValueSource(strings = {"true", "false"})
    void listComments(String verboseString) {
        final boolean verbose = Boolean.valueOf(verboseString);
        final String bookId = BOOK1.getId();
        final List<Comment> comments = Arrays.asList(COMMENT1, COMMENT3);

        // act
        String message = commentCommands.listComments(bookId, verbose);

        assertThat(message).contains(BOOK1.getTitle());
        assertThat(message)
                .contains(COMMENT1.getCommentator()).contains(COMMENT1.getText())
                .contains(COMMENT3.getCommentator()).contains(COMMENT3.getText());

        verify(commentService).findByBookId(bookId);
        verify(commentsFormatter).format(comments, verbose);
    }

    @Test
    void removeComment() {
        final String commentId = COMMENT1.getId();

        String message = commentCommands.removeComment(commentId);

        assertThat(message)
                .contains(String.valueOf(commentId))
                .containsIgnoringCase("removed");

        verify(commentService).delete(commentId);

    }
}