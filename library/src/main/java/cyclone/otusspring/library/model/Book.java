package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class Book {
    private Long bookId;

    private String title;
    private Integer year;

    private Author author;
    private Genre genre;

    public Book(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public Book(String title, Integer year, Author author, Genre genre) {
        this(null, title, year, author, genre);
    }

    public Book(Long bookId, String title, Integer year, Author author, Genre genre) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.bookId = bookId;
        this.title = title;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }
}
