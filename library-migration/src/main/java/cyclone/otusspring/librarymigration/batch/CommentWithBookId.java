package cyclone.otusspring.librarymigration.batch;

import cyclone.otusspring.library.model.Comment;
import lombok.Data;

@Data
public class CommentWithBookId extends Comment {
    private String bookId;

    public CommentWithBookId(String bookId, Comment comment) {
        super(comment.getCommentator(), comment.getText(), comment.getDate());
        this.bookId = bookId;
    }
}
