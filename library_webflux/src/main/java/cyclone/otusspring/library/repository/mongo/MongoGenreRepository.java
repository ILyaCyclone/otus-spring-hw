package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoGenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAllByOrderByName();

    Flux<Genre> findByNameContainingIgnoreCaseOrderByName(String name);
}
