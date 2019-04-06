package cyclone.otusspring.library.dto;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDetails {
    private Long bookId;
    private String title;
    private Integer year;

    private Author author;
    private Genre genre;
}
