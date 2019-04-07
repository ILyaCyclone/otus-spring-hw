package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dao.AuthorDao;
import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dao.GenreDao;
import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    @Transactional
    public Book createBook(BookDto bookDto) {
        Author author = authorDao.findOne(bookDto.getAuthorId());
        Genre genre = genreDao.findOne(bookDto.getGenreId());

        Book newBook = new Book(bookDto.getTitle(), bookDto.getYear(), author, genre);
        return bookDao.save(newBook);
    }
}
