package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<Genre> findAll();

    Mono<Genre> findOne(String id);

    Mono<Genre> save(Genre genre);

    Mono<Void> delete(String id);

}
