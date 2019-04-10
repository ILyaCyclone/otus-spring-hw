package cyclone.otusspring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDto {
    private String title;
    private Integer year;
    private long authorId;
    private long genreId;
}
