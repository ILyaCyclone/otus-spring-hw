package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

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


    public Comment(String commentator, String text) {
        this(null, commentator, text, LocalDateTime.now());
    }

    public Comment(String commentator, String text, LocalDateTime date) {
        this(null, commentator, text, date);
    }

    public Comment(String id, String commentator, String text, LocalDateTime date) {
        if (StringUtils.isEmpty(commentator)) {
            throw new IllegalArgumentException("commentator must not be empty");
        }
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("comment text must not be empty");
        }

        this.id = id;
        this.commentator = commentator;
        this.text = text;
        this.date = date;
    }
}
