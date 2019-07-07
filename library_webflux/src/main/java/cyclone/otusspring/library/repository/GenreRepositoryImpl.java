package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.mongo.MongoGenreRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final MongoGenreRepository mongoRepository;

    public GenreRepositoryImpl(MongoGenreRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


    @Override
    public Flux<Genre> findAll() {
        return mongoRepository.findAllByOrderByName();
    }

    @Override
    public Flux<Genre> findByName(String name) {
        return mongoRepository.findByNameContainingIgnoreCaseOrderByName(name);
    }

    @Override
    public Mono<Genre> findOne(String id) {
        return mongoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Genre ID " + id + " not found")));
    }

    @Override
    public Mono<Genre> save(Genre genre) {
        return mongoRepository.save(genre);
    }

    @Override
    public Mono<Void> delete(String id) {
        return Mono.just(id)
                .filterWhen(mongoRepository::existsById)
                .switchIfEmpty(Mono.error(new NotFoundException("Genre ID " + id + " not found")))
                .flatMap(mongoRepository::deleteById);
    }

    @Override
    public Mono<Boolean> exists(String id) {
        return mongoRepository.existsById(id);
    }
}
