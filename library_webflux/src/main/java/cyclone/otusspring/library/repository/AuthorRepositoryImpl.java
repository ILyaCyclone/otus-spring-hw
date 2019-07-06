package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final MongoAuthorRepository mongoRepository;

    public AuthorRepositoryImpl(MongoAuthorRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


    @Override
    public List<Author> findAll() {
        return mongoRepository.findAll(Sort.by("firstname", "lastname"));
    }

    @Override
    public List<Author> findByName(String name) {
        return mongoRepository.findByName(name);
    }

    @Override
    public Author findOne(String id) {
        return mongoRepository.findById(id).orElseThrow(() -> new NotFoundException("Author ID " + id + " not found"));
    }

    @Override
    public Author save(Author author) {
        return mongoRepository.save(author);
    }

    @Override
    public void delete(String id) {
        if (!mongoRepository.existsById(id)) throw new NotFoundException("Author ID " + id + " not found");
        mongoRepository.deleteById(id);
    }

    @Override
    public void delete(Author author) {
        mongoRepository.delete(author);
    }

    @Override
    public boolean exists(String id) {
        return mongoRepository.existsById(id);
    }
}
