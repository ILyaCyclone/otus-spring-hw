package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreRepository {
    Flux<Genre> findAll();

    Flux<Genre> findByName(String name);

    Mono<Genre> findOne(String id);

    Mono<Genre> save(Genre Genre);

    Mono<Void> delete(String id);

    Mono<Boolean> exists(String id);
}
