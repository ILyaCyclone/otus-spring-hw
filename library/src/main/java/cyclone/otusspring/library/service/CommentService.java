package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByBookId(long bookId);

    Comment create(CommentDto commentDto);

    void delete(long commentId);
}
