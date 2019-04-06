package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class Book {
    private Long bookId;
    private long authorId;
    private long genreId;

    private String title;
    private Integer year;

    public Book(long authorId, long genreId, String title, Integer year) {
        this(null, authorId, genreId, title, year);
    }

    public Book(Long bookId, long authorId, long genreId, String title, Integer year) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.bookId = bookId;
        this.authorId = authorId;
        this.genreId = genreId;
        this.title = title;
        this.year = year;
    }
}
