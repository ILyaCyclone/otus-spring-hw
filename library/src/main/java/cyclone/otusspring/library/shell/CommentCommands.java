package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import java.util.List;

@ShellComponent
public class CommentCommands {

    private final CommentService commentService;
    private final BookService bookService;
    private final CommentsFormatter commentsFormatter;

    @Autowired
    AuthenticationManager authenticationManager;

    public CommentCommands(CommentService commentService, BookService bookService, CommentsFormatter commentsFormatter) {
        this.commentService = commentService;
        this.bookService = bookService;
        this.commentsFormatter = commentsFormatter;
    }

    String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }


    @ShellMethod(value = "Sign in")
    String signIn(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "Signed in as " + getCurrentUserName();
        } catch (AuthenticationException e) {
            return "Could not authenticate: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Sign out")
    String signOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
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
}
