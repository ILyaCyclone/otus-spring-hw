package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoCommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookOrderByDate(Book book);

    List<Comment> findByBookIdOrderByDate(String bookId);

    List<Comment> findByCommentatorOrderByDate(String commentator);
}
