package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final MongoAuthorRepository mongoRepository;

    public AuthorRepositoryImpl(MongoAuthorRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


    @Override
    public Flux<Author> findAll() {
        return mongoRepository.findAll(Sort.by("firstname", "lastname"));
    }

    @Override
    public Flux<Author> findByName(String name) {
        return mongoRepository.findByName(name);
    }

    @Override
    public Mono<Author> findOne(String id) {
        return mongoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Author ID " + id + " not found")));
    }

    @Override
    public Mono<Author> save(Author author) {
        return mongoRepository.save(author);
    }

    @Override
    public Mono<Void> delete(String id) {
        //TODO unblock
        if (!mongoRepository.existsById(id).block()) {
            throw new NotFoundException("Author ID " + id + " not found");
        }
        return mongoRepository.deleteById(id);

        // hangs
//        return mongoRepository.existsById(id)
//            .flatMap(exists -> {
//                if(!exists) {
//                    throw new NotFoundException("Author ID " + id + " not found");
//                }
//                return mongoRepository.deleteById(id);
//            });


//        return Mono.just(id)
//                .filterWhen(mongoRepository::existsById)
//                .switchIfEmpty(Mono.error(new NotFoundException("Author ID " + id + " not found")))
//                .flatMap(mongoRepository::deleteById)
    }

    @Override
    public Mono<Boolean> exists(String id) {
        return mongoRepository.existsById(id);
    }
}
