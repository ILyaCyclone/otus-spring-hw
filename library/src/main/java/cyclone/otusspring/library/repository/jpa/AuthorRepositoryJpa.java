package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class AuthorRepositoryJpa implements AuthorRepository {

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
            throw new EntityNotFoundException("Author id " + id + " not found");
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
        return em.createQuery("select case when count(a) > 0 then TRUE else FALSE end from Author a where a.authorId = :id", Boolean.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
