package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {
    Mono<Book> findOne(String bookId);

    Flux<Book> findAll();

    Mono<Book> save(Book book);

    Mono<BookWithoutComments> save(BookWithoutComments book);

    Mono<Void> delete(String id);

    Flux<Book> findByAuthor(Author author);

    Flux<Book> findByGenre(Genre genre);
}