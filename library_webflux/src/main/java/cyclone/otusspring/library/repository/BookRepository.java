package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository {
    Flux<Book> findAll();

    Flux<Book> findByTitle(String title);

    Flux<Book> findByAuthor(Author author);

    Flux<Book> findByGenre(Genre genre);

    Mono<Book> findOne(String id);

    Mono<Book> save(Book book);

    BookWithoutComments save(BookWithoutComments book);

    Mono<Void> delete(String id);

    Mono<Boolean> exists(String id);
}
