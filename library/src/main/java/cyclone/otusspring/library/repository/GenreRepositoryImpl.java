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

    private final MongoGenreRepository mongoRepository;

    public GenreRepositoryImpl(MongoGenreRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


    @Override
    public List<Genre> findAll() {
        return mongoRepository.findAllByOrderByName();
    }

    @Override
    public List<Genre> findByName(String name) {
        return mongoRepository.findByNameContainingIgnoreCaseOrderByName(name);
    }

    @Override
    public Genre findOne(String id) {
        return mongoRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre ID " + id + " not found"));
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        return mongoRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!mongoRepository.existsById(id)) throw new NotFoundException("Genre ID " + id + " not found");
        mongoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        mongoRepository.delete(genre);
    }

    @Override
    public boolean exists(String id) {
        return mongoRepository.existsById(id);
    }
}
