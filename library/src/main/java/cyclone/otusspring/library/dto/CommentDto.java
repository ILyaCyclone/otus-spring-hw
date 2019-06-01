package cyclone.otusspring.library.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class CommentDto {
    private String id;
    private String bookId;
    private String commentator;
    private String text;
    private LocalDateTime date;

    public CommentDto() {
    }

    public CommentDto(String bookId, String commentator, String text) {
        this.bookId = bookId;
        this.commentator = commentator;
        this.text = text;
    }

    public CommentDto(String bookId, String commentator, String text, LocalDateTime date) {
        this.bookId = bookId;
        this.commentator = commentator;
        this.text = text;
        this.date = date;
    }

    public CommentDto(String id, String bookId, String commentator, String text, LocalDateTime date) {
        this.id = id;
        this.bookId = bookId;
        this.commentator = commentator;
        this.text = text;
        this.date = date;
    }
}
