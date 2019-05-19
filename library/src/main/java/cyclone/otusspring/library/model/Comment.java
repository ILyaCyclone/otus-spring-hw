package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

//TODO Remove @Document, should be nested collection
@Document("comments")
@Data
@NoArgsConstructor
public class Comment {
    @Id
    private String id;

    @Field("commentator")
    private String commentator;

    @Field("text")
    private String text;

    @Field("date")
    private LocalDateTime date;

    //TODO Remove @DBRef, should be nested collection
    @DBRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;


    public Comment(String commentator, String text, Book book) {
        this(null, commentator, text, LocalDateTime.now(), book);
    }

    public Comment(String commentator, String text, LocalDateTime date, Book book) {
        this(null, commentator, text, date, book);
    }

    public Comment(String id, String commentator, String text, LocalDateTime date, Book book) {
        if (StringUtils.isEmpty(commentator)) {
            throw new IllegalArgumentException("commentator must not be empty");
        }
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("comment text must not be empty");
        }
        Objects.requireNonNull(book, "comment book must not be null");

        this.id = id;
        this.commentator = commentator;
        this.text = text;
        this.date = date;
        this.book = book;
    }
}
