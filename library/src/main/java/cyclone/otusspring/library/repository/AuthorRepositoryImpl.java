package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.datajpa.AuthorJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository jpaRepository;

    public AuthorRepositoryImpl(AuthorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public List<Author> findAll() {
        return jpaRepository.findAll(Sort.by("firstname", "lastname"));
    }

    @Override
    public List<Author> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    @Override
    public Author findOne(long id) {
        return jpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author ID " + id + " not found"));
    }

    @Override
    public Author save(Author author) {
        return jpaRepository.save(author);
    }

    @Override
    public void delete(long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(Author author) {
        jpaRepository.delete(author);
    }

    @Override
    public boolean exists(long id) {
        return jpaRepository.existsById(id);
    }
}
