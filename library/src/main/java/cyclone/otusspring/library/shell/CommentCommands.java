package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.CommentService;
import lombok.Getter;
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

    @Getter
    private String currentUser;

    public CommentCommands(CommentService commentService, BookService bookService, CommentsFormatter commentsFormatter) {
        this.commentService = commentService;
        this.bookService = bookService;
        this.commentsFormatter = commentsFormatter;
    }


    @ShellMethod(value = "Sign in")
    String signIn(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not be empty");
        }

        currentUser = name;
        return "Signed in as " + currentUser;
    }

    @ShellMethod(value = "Sign out")
    String signOut() {
        currentUser = null;
        return "Signed out";
    }

    @ShellMethod(value = "Add comment")
    String addComment(String bookId, String text) {
        CommentDto commentDto = new CommentDto(bookId, currentUser, text);
        commentService.create(commentDto);
        return "Your comment saved";
    }

    public Availability addCommentAvailability() {
        return StringUtils.hasText(currentUser)
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
    String removeComment(String commentId) {
        commentService.delete(commentId);
        return "Comment ID " + commentId + " removed";
    }
}
