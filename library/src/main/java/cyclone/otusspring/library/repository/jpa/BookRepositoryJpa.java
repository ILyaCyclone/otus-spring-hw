package cyclone.otusspring.library.repository.jpa;


import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.repository.BookRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b order by b.title", Book.class).getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return em.createQuery("select b from Book b where lower(b.title) like concat('%', concat(lower(:title), '%')) order by b.title", Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public Book findOne(long id) {
        Book book = em.find(Book.class, id);
        if (book == null) {
            throw new EntityNotFoundException("Book id " + id + " not found");
        }
        return book;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (Objects.isNull(book.getBookId())) {
            // create new entity
            em.persist(book); // ID will be populated
//            return findOne(book.getBookId());
            return book;
        } else {
            // edit existing entity
//            Book mergedBook = em.merge(book);
//            return findOne(mergedBook.getBookId());

            return em.merge(book);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        delete(em.getReference(Book.class, id));
    }

    @Override
    @Transactional
    public void delete(Book book) {
        em.remove(book);
    }

    @Override
    public boolean exists(long id) {
        return em.createQuery("select case when count(b) > 0 then TRUE else FALSE end from Book b where b.bookId = :id", Boolean.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
