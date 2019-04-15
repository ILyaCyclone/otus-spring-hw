package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book createBook(BookDto bookDto) {
        long authorId = bookDto.getAuthorId();
        long genreId = bookDto.getGenreId();

        Author author = authorRepository.findOne(authorId);
        Genre genre = genreRepository.findOne(genreId);

        Book newBook = new Book(bookDto.getTitle(), bookDto.getYear(), author, genre);
        return bookRepository.save(newBook);
    }
}
