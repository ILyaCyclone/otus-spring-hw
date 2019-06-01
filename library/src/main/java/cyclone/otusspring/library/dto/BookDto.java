package cyclone.otusspring.library.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {
    private String id;
    private String title;
    private Integer year;
    private String authorId;
    private String genreId;
    private List<CommentDto> commentDtoList = new ArrayList<>();

    public BookDto() {
    }

    public BookDto(String title, Integer year, String authorId, String genreId) {
        this(null, title, year, authorId, genreId);
    }

    public BookDto(String id, String title, Integer year, String authorId, String genreId) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public BookDto(String id, String title, Integer year, String authorId, String genreId, List<CommentDto> commentDtoList) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.authorId = authorId;
        this.genreId = genreId;
        this.commentDtoList = commentDtoList;
    }
}
