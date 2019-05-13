package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AuthorRepositoryImpl implements AuthorRepository {

    private final MongoAuthorRepository jpaRepository;

    public AuthorRepositoryImpl(MongoAuthorRepository jpaRepository) {
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
    public Author findOne(String id) {
        return jpaRepository.findById(id).orElseThrow(() -> new NotFoundException("Author ID " + id + " not found"));
    }

    @Override
    public Author save(Author author) {
        return jpaRepository.save(author);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(Author author) {
        jpaRepository.delete(author);
    }

    @Override
    public boolean exists(String id) {
        return jpaRepository.existsById(id);
    }
}
