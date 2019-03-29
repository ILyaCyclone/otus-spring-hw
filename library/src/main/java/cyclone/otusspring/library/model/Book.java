package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Book {
    private long bookId = -1;
    private long authorId;
    private long genreId;

    private String title;
    private Integer year;

    public Book(long authorId, long genreId, String title, Integer year) {
        Objects.requireNonNull(title, "book title must not be null");
        Objects.requireNonNull(authorId, "book authorId must not be null");
        Objects.requireNonNull(genreId, "book genreId must not be null");

        this.authorId = authorId;
        this.genreId = genreId;
        this.title = title;
        this.year = year;
    }
}
