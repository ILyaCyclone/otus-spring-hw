package cyclone.otusspring.library.dao.jpa;


import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
@Profile(DataAccessProfiles.JPA)
@Transactional(readOnly = true)
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b order by b.title", Book.class).getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return em.createQuery("select b from Book b where lower(b.title) like lower('%'||:title||'%') order by b.title", Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public Book findOne(long id) {
        // method should throw an exception when no objects found
        //TODO decide variant

        // variant 1. em.find returns null if no objects found, process it
        Book book = em.find(Book.class, id);
        if (book == null) {
            throw new IncorrectResultSizeDataAccessException("Book id " + id + " not found", 1, 0);
        }
        return book;

        // variant 2. em...getSingleResult throws NoResultException or NonUniqueResultException if result count is not 1
//        return em.createQuery("select b from Book b where b.bookId = :id", Book.class)
//                .setParameter("id", id)
//                .getSingleResult();

        // variant 3. Spring DataAccessUtils.singleResult throws IncorrectResultSizeDataAccessException if result count is not 1
//        return DataAccessUtils.singleResult(em.createQuery("select b from Book b where b.bookId = :id", Book.class)
//                .setParameter("id", id).getResultList());
    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (Objects.isNull(book.getBookId())) {
            // create new entity
            em.persist(book);
            em.flush(); // ID will be populated
            return book;
        } else {
            // edit existing entity
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
        return em.find(Book.class, id) != null;
    }
}
