package cyclone.otusspring.library.repository;


import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final MongoBookRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Flux<Book> findAll() {
        return mongoRepository.findAllByOrderByTitle();
    }

    @Override
    public Flux<Book> findByTitle(String title) {
        return mongoRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    @Override
    public Flux<Book> findByAuthor(Author author) {
        return mongoRepository.findByAuthorOrderByTitle(author);
    }

    @Override
    public Flux<Book> findByGenre(Genre genre) {
        return mongoRepository.findByGenreOrderByTitle(genre);
    }

    @Override
    public Mono<Book> findOne(String id) {
        return mongoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Book ID " + id + " not found")));
    }

    @Override
    public Mono<Book> save(Book book) {
        return mongoRepository.save(book);
    }

    @Override
    public Mono<BookWithoutComments> save(BookWithoutComments bookWithoutComments) {
        String id = bookWithoutComments.getId();
        //TODO unblock
        if (id != null && mongoRepository.existsById(id).block()) {
            Update update = new Update();
            Document document = (Document) (mongoTemplate.getConverter().convertToMongoType(bookWithoutComments));
            document.entrySet().forEach(entry -> update.set(entry.getKey(), entry.getValue()));

            mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, BookWithoutComments.class);
            return Mono.just(bookWithoutComments);
        } else {
            return Mono.just(mongoTemplate.save(bookWithoutComments));
        }
    }

    @Override
    public Mono<Void> delete(String id) {
        return Mono.just(id)
                .filterWhen(mongoRepository::existsById)
                .switchIfEmpty(Mono.error(new NotFoundException("Book ID " + id + " not found")))
                .flatMap(mongoRepository::deleteById);
    }

    @Override
    public Mono<Boolean> exists(String id) {
        return mongoRepository.existsById(id);
    }
}
