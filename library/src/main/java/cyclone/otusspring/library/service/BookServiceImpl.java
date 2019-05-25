package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Book findOne(String bookId) {
        return bookRepository.findOne(bookId);
    }


    @Override
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
    public Book create(BookDto bookDto) {
        String authorId = bookDto.getAuthorId();
        String genreId = bookDto.getGenreId();

        if (!authorRepository.exists(authorId)) {
            throw new RuntimeException("Could not create book", new NotFoundException("Author ID " + authorId + " not found"));
        }
        if (!genreRepository.exists(genreId)) {
            throw new RuntimeException("Could not create book", new NotFoundException("Genre ID " + genreId + " not found"));
        }

        Author author = authorRepository.findOne(authorId);
        Genre genre = genreRepository.findOne(genreId);

        Book bookToCreate = new Book(bookDto.getTitle(), bookDto.getYear(), author, genre);
        return bookRepository.save(bookToCreate);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
