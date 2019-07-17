package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorRepository {
    Flux<Author> findAll();

    Flux<Author> findByName(String name);

    Mono<Author> findOne(String id);

    Mono<Author> save(Author author);

    Mono<Void> delete(String id);

    Mono<Boolean> exists(String id);
}
