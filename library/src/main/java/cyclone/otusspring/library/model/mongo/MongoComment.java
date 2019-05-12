package cyclone.otusspring.library.model.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

//@Document("comments")
@Data
@NoArgsConstructor
public class MongoComment {
    @Id
    private String commentId;

    @Field("commentator")
    private String commentator;

    @Field("text")
    private String text;

    @Field("date")
    private LocalDateTime date;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MongoBook book;


    public MongoComment(String commentator, String text, MongoBook book) {
        this(null, commentator, text, LocalDateTime.now(), book);
    }

    public MongoComment(String commentator, String text, LocalDateTime date, MongoBook book) {
        this(null, commentator, text, date, book);
    }

    public MongoComment(String commentId, String commentator, String text, LocalDateTime date, MongoBook book) {
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
