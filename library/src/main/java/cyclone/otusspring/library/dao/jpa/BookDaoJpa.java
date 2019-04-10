package cyclone.otusspring.library.dao.jpa;


import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Profile(DataAccessProfiles.JPA)
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b order by b.title", Book.class).getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public Book findOne(long id) {
        return null;
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public boolean exists(long id) {
        return false;
    }
}
