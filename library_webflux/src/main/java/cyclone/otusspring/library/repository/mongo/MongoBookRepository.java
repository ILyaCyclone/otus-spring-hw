package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoBookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findAllByOrderByTitle();

    Flux<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);

    Flux<Book> findByAuthorOrderByTitle(Author author);

    Flux<Book> findByGenreOrderByTitle(Genre genre);

}
