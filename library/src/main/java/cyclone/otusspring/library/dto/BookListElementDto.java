package cyclone.otusspring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookListElementDto {
    private String id;
    private String title;
    private Integer year;
    private String authorFirstname;
    private String authorLastname;
    private String genreName;
}
