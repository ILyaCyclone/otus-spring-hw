package cyclone.otusspring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDto {
    private String title;
    private Integer year;
    private String authorId;
    private String genreId;
}
