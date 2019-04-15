package cyclone.otusspring.library.repository.jpa;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g order by g.name", Genre.class).getResultList();
    }

    @Override
    public List<Genre> findByName(String name) {
        return em.createQuery("select g from Genre g  " +
                "where lower(name) like '%'||lower(:name)||'%' " +
                "order by name", Genre.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Genre findOne(long id) {
        Genre genre = em.find(Genre.class, id);
        if (genre == null) {
            throw new IncorrectResultSizeDataAccessException("Genre id " + id + " not found", 1, 0);
        }
        return genre;
    }

    @Override
    public Genre save(Genre genre) {
        if (Objects.isNull(genre.getGenreId())) {
            // create new entity
            em.persist(genre);
            em.flush(); // ID will be populated
            return genre;
        } else {
            // edit existing entity
            return em.merge(genre);
        }
    }

    @Override
    public void delete(long id) {
        delete(em.getReference(Genre.class, id));
    }

    @Override
    public void delete(Genre genre) {
        em.remove(genre);
    }

    @Override
    public boolean exists(long id) {
        return em.find(Genre.class, id) != null;
    }
}
