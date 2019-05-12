package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.mongo.MongoCommentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CommentRepositoryImpl implements CommentRepository {

    private final MongoCommentRepository repository;

    public CommentRepositoryImpl(MongoCommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment findOne(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Comment ID " + id + " not found"));
    }

    @Override
    public List<Comment> findByBook(Book book) {
        return repository.findByBookOrderByDate(book);
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        return repository.findByBookIdOrderByDate(bookId);
    }

    @Override
    public List<Comment> findByCommentator(String commentator) {
        return repository.findByCommentatorOrderByDate(commentator);
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Comment comment) {
        repository.delete(comment);
    }
}
