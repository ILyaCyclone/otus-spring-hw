package cyclone.otusspring.library.dao.jpa;

import cyclone.otusspring.library.dao.AuthorDao;
import cyclone.otusspring.library.dao.DataAccessProfiles;
import cyclone.otusspring.library.model.Author;
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
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a order by a.firstname, a.lastname", Author.class).getResultList();
    }

    @Override
    public List<Author> findByName(String name) {
        return em.createQuery("select a from Author a " +
                "where lower(a.firstname) like lower('%'||:name||'%') " +
                "or lower(a.lastname) like lower('%'||:name||'%') " +
                "order by a.firstname, a.lastname", Author.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Author findOne(long id) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            throw new IncorrectResultSizeDataAccessException("Author id " + id + " not found", 1, 0);
        }
        return author;
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (Objects.isNull(author.getAuthorId())) {
            // create new entity
            em.persist(author);
            em.flush(); // ID will be populated
            return author;
        } else {
            // edit existing entity
            return em.merge(author);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        delete(em.getReference(Author.class, id));
    }

    @Override
    @Transactional
    public void delete(Author author) {
        em.remove(author);
    }

    @Override
    public boolean exists(long id) {
        return em.find(Author.class, id) != null;
    }
}
