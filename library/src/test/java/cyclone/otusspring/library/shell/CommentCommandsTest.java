package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(ResetStateExtension.class)
class CommentCommandsTest {

    @Autowired
    CommentCommands commentCommands;

    @Autowired
    MongoTemplate mongoTemplate;


    @Test
    void signIn() {
        String message = commentCommands.signIn("user1", "user1");

        assertThat(commentCommands.getCurrentUserName()).isEqualTo("user1");
        assertThat(message)
                .containsIgnoringCase("signed in")
                .contains("user1");
    }

    @Test
    void signOut() {
        String message = commentCommands.signOut();

        assertThat(commentCommands.getCurrentUserName()).isNull();
        assertThat(message).containsIgnoringCase("signed out");
    }

    @Test
    void addComment() {
        final String username = "user1";
        final String commentText = "new comment";

        commentCommands.signIn(username, username);
        //act
        String message = commentCommands.addComment(BOOK1.getId(), commentText);

        List<Comment> actualComments = mongoTemplate.findById(BOOK1.getId(), Book.class).getComments();
        assertThat(actualComments).usingElementComparatorIgnoringFields("id", "date")
                .contains(new Comment(username, commentText));
        assertThat(message).containsIgnoringCase("saved");
    }

    @ParameterizedTest
    @DisplayName("listComments (with --verbose and without)")
    @ValueSource(strings = {"true", "false"})
    void listComments(String verboseString) {
        final boolean verbose = Boolean.valueOf(verboseString);
        final String bookId = BOOK1.getId();

        // act
        String message = commentCommands.listComments(bookId, verbose);

        assertThat(message).contains(BOOK1.getTitle());
        assertThat(message)
                .contains(COMMENT1.getCommentator()).contains(COMMENT1.getText())
                .contains(COMMENT3.getCommentator()).contains(COMMENT3.getText());
    }

    @Test
    void removeComment() {
        final String commentId = COMMENT1.getId();

        //act
        String message = commentCommands.removeComment(BOOK1.getId(), commentId);

        List<Comment> actualComments = mongoTemplate.findById(BOOK1.getId(), Book.class).getComments();
        List<String> commentIds = actualComments.stream().map(Comment::getId).collect(Collectors.toList());

        assertThat(commentIds).doesNotContain(commentId);
        assertThat(message)
                .contains(String.valueOf(commentId))
                .containsIgnoringCase("removed");
    }
}