package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toComment(CommentDto dto) {
        return new Comment(dto.getId(), dto.getCommentator(), dto.getText(), dto.getDate());
    }

    public CommentDto toCommentDto(Comment comment, String bookId) {
        return new CommentDto(comment.getId(), bookId, comment.getCommentator(), comment.getText(), comment.getDate());
    }

}
