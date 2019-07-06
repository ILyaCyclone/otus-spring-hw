package cyclone.otusspring.library.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookFormDto {
    BookDto bookDto;
    List<AuthorDto> allAuthors;
    List<GenreDto> allGenres;
}
