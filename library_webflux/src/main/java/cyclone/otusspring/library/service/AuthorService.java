package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {
    Flux<Author> findAll();

    Mono<Author> findOne(String id);

    Mono<Author> save(Author author);

    Mono<Void> delete(String id);
}
