package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class CommentsFormatter {
    public String format(List<Comment> comments) {
        return format(comments, false);
    }

    public String format(List<Comment> comments, boolean verbose) {
        return verbose ? formatCommentsAsTable(comments) : formatCommentsHumanFriendly(comments);
    }

    private String formatCommentsHumanFriendly(List<Comment> comments) {
        return "Comments:\n" + comments.stream()
                .map(comment -> comment.getCommentator() + " at " + comment.getDate() + " says: " + comment.getText())
                .collect(Collectors.joining("\n"));
    }

    private String formatCommentsAsTable(List<Comment> comments) {
        return new SimpleTableBuilder()
                .data(comments)
                .addHeader("commentId", "ID")
                .addHeader("commentator", "Commentator")
                .addHeader("date", "Date")
                .addHeader("text", "Text")
                .build()
                .render(100);
    }
}
