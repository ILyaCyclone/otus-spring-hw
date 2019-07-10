package cyclone.otusspring.library.service;

import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Mono<Book> findOne(String bookId) {
        return bookRepository.findOne(bookId);
    }


    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Flux<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Flux<Book> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Mono<Book> save(Book book) {
//        Boolean bookAuthorExists = authorRepository.exists(book.getAuthor().getId()).block();
//        if (!bookAuthorExists) {
//            throw new RuntimeException("Could not save book"
//                    , new NotFoundException("Author ID " + book.getAuthor().getId() + " not found"));
//
//        }
//        Boolean bookGenreExists = genreRepository.exists(book.getGenre().getId()).block();
//        if (!bookGenreExists) {
//            throw new RuntimeException("Could not save book"
//                    , new NotFoundException("Genre ID " + book.getGenre().getId() + " not found"));
//
//        }
//        return bookRepository.save(book);

        return Mono.zip(
                authorRepository.exists(book.getAuthor().getId())
                , genreRepository.exists(book.getGenre().getId())
        )
                .flatMap(tuple2 -> {
                    if (!tuple2.getT1()) {
                        return Mono.error(new RuntimeException("Could not save book"
                                , new NotFoundException("Author ID " + book.getAuthor().getId() + " not found")));
                    }
                    if (!tuple2.getT2()) {
                        return Mono.error(new RuntimeException("Could not save book"
                                , new NotFoundException("Genre ID " + book.getGenre().getId() + " not found")));
                    }
                    return bookRepository.save(book);
                });
    }

    @Override
    public Mono<BookWithoutComments> save(BookWithoutComments bookWithoutComments) {
        return bookRepository.save(bookWithoutComments);
    }

    @Override
    public Mono<Void> delete(String id) {
        return bookRepository.delete(id);
    }
}
