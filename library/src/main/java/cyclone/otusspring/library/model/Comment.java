package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long commentId;

    private String commentator;
    private String text;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Comment(String commentator, String text, LocalDateTime date, Book book) {
        this(null, commentator, text, date, book);
    }
    public Comment(Long commentId, String commentator, String text, LocalDateTime date, Book book) {
        if (StringUtils.isEmpty(commentator)) {
            throw new IllegalArgumentException("commentator must not be empty");
        }
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("comment text must not be empty");
        }
        Objects.requireNonNull(book, "comment book must not be null");

        this.commentId = commentId;
        this.commentator = commentator;
        this.text = text;
        this.date = date;
        this.book = book;
    }
}
