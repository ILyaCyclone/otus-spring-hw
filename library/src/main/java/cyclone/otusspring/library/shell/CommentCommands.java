package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.AuthenticationService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;
    private final BookService bookService;
    private final CommentsFormatter commentsFormatter;

    private final AuthenticationService authenticationService;



    @ShellMethod(value = "Sign in")
    String signIn(String username, String password) {
        try {
            String authenticatedUsername = authenticationService.authenticate(username, password);

            return "Signed in as " + authenticatedUsername;
        } catch (AuthenticationException e) {
            return "Could not authenticate: " + e.getMessage();
        }
    }


    @ShellMethod(value = "Sign out")
    String signOut() {
        authenticationService.logout();
        return "Signed out";
    }

    @ShellMethod(value = "Add comment")
    String addComment(String bookId, String text) {
        CommentDto commentDto = new CommentDto(bookId, getCurrentUserName(), text);
        commentService.create(commentDto);
        return "Your comment saved";
    }

    public Availability addCommentAvailability() {
        return StringUtils.hasText(getCurrentUserName())
                ? Availability.available()
                : Availability.unavailable("you need to sign in to leave comments");
    }

    @ShellMethod(value = "List book comments")
    String listComments(String bookId, boolean verbose) {
        Book book = bookService.findOne(bookId);
        List<Comment> comments = commentService.findByBookId(bookId);

        String message = String.format("\"%s\" by %s (%d)", book.getTitle()
                , (book.getAuthor().getFirstname() + " " + book.getAuthor().getLastname())
                , book.getYear());

        String commentsMessage;
        if (!comments.isEmpty()) {
            commentsMessage = commentsFormatter.format(comments, verbose);
        } else {
            commentsMessage = "No comments yet.";
        }
        return message + "\n" + commentsMessage;
    }

    @ShellMethod(value = "Remove comment")
    String removeComment(String bookId, String commentId) {
        commentService.delete(bookId, commentId);
        return "Comment ID " + commentId + " removed";
    }



    public String getCurrentUserName() {
        return authenticationService.getCurrentUsername();
    }
}
