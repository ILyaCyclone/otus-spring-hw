package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment findOne(long commentId);

    List<Comment> findByBook(Book book);

    List<Comment> findByBookId(long bookId);

    List<Comment> findByCommentator(String commentator);

    Comment save(Comment comment);

    void delete(long commentId);

    void delete(Comment comment);
}
