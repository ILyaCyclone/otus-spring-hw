package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Author;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoAuthorRepository extends ReactiveMongoRepository<Author, String> {

    @Query(value = "{$or: [ {'firstname' : { $regex: ?0, $options: 'i'}},  {'lastname' : { $regex: ?0, $options: 'i'}}]}"
            , sort = "{firstname: 1, lastname: 1}")
    Flux<Author> findByName(String name);

    Mono<Author> save(Mono<Author> author);

    //    Mono<Void> delete(Mono<Author> author);
//
    Mono<Void> deleteById(String id);
}
