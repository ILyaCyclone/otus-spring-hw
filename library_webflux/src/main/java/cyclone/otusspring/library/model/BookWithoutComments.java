package cyclone.otusspring.library.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

@Document(collection = "books")
@Data
@NoArgsConstructor
public class BookWithoutComments {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("year")
    private Integer year;

    @DBRef
    @Field("author")
    private Author author;

    @DBRef
    @Field("genre")
    private Genre genre;

    public BookWithoutComments(String id, String title, Integer year, Author author, Genre genre) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

}
