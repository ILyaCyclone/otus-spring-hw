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
public class Book {
//    public static final String GRAPH_WITH_AUTHOR_GENRE = "with-author-genre";

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("year")
    private Integer year;

    @DBRef
    private MongoAuthor author;

    @DBRef
    private Genre genre;

    //    @OneToMany(
//            mappedBy = "book",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY
//    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();


    public Book(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public Book(String title, Integer year, MongoAuthor author, Genre genre) {
        this(null, title, year, author, genre);
    }

    public Book(String id, String title, Integer year, MongoAuthor author, Genre genre) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBook(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBook(null);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", author=" + author +
                ", genre=" + genre +
                '}';
    }
}
