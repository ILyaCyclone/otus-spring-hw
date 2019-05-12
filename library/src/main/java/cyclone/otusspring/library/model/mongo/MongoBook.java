package cyclone.otusspring.library.model.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "books")
//@NamedEntityGraph(
//        name = GRAPH_WITH_AUTHOR_GENRE,
//        attributeNodes = {
//                @NamedAttributeNode("author"),
//                @NamedAttributeNode("genre")
//        }
//)
@Data
@NoArgsConstructor
public class MongoBook {
//    public static final String GRAPH_WITH_AUTHOR_GENRE = "with-author-genre";

    @Id
    private String bookId;

    @Field("title")
    private String title;

    @Field("year")
    private Integer year;

    @DBRef
    private MongoAuthor author;

    @DBRef
    private MongoGenre genre;

    //    @OneToMany(
//            mappedBy = "book",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY
//    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<MongoComment> comments = new ArrayList<>();


    public MongoBook(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public MongoBook(String title, Integer year, MongoAuthor author, MongoGenre genre) {
        this(null, title, year, author, genre);
    }

    public MongoBook(String bookId, String title, Integer year, MongoAuthor author, MongoGenre genre) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.bookId = bookId;
        this.title = title;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public void addComment(MongoComment comment) {
        comments.add(comment);
        comment.setBook(this);
    }

    public void removeComment(MongoComment comment) {
        comments.remove(comment);
        comment.setBook(null);
    }
}
