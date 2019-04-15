package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.CommentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Comment findOne(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new EntityNotFoundException("Comment id " + id + " not found");
        }
        return comment;
    }

    @Override
    public List<Comment> findByBook(Book book) {
        return em.createQuery("select c from Comment c where c.book = :book order by c.date")
                .setParameter("book", book)
                .getResultList();
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        return em.createQuery("select c from Comment c where c.book.bookId = :bookId order by c.date")
                .setParameter("bookId", bookId)
                .getResultList();
    }

    @Override
    public List<Comment> findByCommentator(String commentator) {
        return em.createQuery("select c from Comment c where lower(c.commentator) = lower(:commentator) order by c.date")
                .setParameter("commentator", commentator)
                .getResultList();
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        if (Objects.isNull(comment.getCommentId())) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        delete(em.getReference(Comment.class, id));
    }

    @Override
    @Transactional
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
