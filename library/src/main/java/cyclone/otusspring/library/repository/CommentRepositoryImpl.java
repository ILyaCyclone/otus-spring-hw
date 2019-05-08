package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.datajpa.CommentJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    public CommentRepositoryImpl(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comment findOne(long id) {
        return jpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment ID " + id + " not found"));
    }

    @Override
    public List<Comment> findByBook(Book book) {
        return jpaRepository.findByBookOrderByDate(book);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        return jpaRepository.findByBookBookIdOrderByDate(bookId);
    }

    @Override
    public List<Comment> findByCommentator(String commentator) {
        return jpaRepository.findByCommentatorOrderByDate(commentator);
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        return jpaRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Comment comment) {
        jpaRepository.delete(comment);
    }
}
