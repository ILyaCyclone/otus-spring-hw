package cyclone.otusspring.library.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CommentDto {
    private long bookId;
    private String commentator;
    private String text;
}
