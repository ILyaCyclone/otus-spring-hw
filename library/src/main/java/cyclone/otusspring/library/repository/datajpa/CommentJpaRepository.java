package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBookOrderByDate(Book book);

    List<Comment> findByBookBookIdOrderByDate(long bookId);

    List<Comment> findByCommentatorOrderByDate(String commentator);

}
