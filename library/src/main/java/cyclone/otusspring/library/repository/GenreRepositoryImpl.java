package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.mongo.MongoGenreRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class GenreRepositoryImpl implements GenreRepository {

    private final MongoGenreRepository repository;

    public GenreRepositoryImpl(MongoGenreRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Genre> findAll() {
        return repository.findAllByOrderByName();
    }

    @Override
    public List<Genre> findByName(String name) {
        return repository.findByNameContainingIgnoreCaseOrderByName(name);
    }

    @Override
    public Genre findOne(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Genre ID " + id + " not found"));
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        return repository.save(genre);
    }

    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        repository.delete(genre);
    }

    @Override
    public boolean exists(String id) {
        return repository.existsById(id);
    }
}
