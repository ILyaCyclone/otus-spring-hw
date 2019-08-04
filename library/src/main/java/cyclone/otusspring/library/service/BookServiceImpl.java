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
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Book findOne(String bookId) {
        return bookRepository.findOne(bookId);
    }


    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Book save(Book book) {
        if (!authorRepository.exists(book.getAuthor().getId())) {
            throw new RuntimeException("Could not save book"
                    , new NotFoundException("Author ID " + book.getAuthor().getId() + " not found"));

        }
        if (!genreRepository.exists(book.getGenre().getId())) {
            throw new RuntimeException("Could not save book"
                    , new NotFoundException("Genre ID " + book.getGenre().getId() + " not found"));

        }
        return bookRepository.save(book);
    }

    @Override
    public BookWithoutComments save(BookWithoutComments bookWithoutComments) {
        return bookRepository.save(bookWithoutComments);
    }

    @Override
    public void delete(String id) {
        bookRepository.delete(id);
    }
}
