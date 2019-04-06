package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dao.BookDao;
import cyclone.otusspring.library.dto.BookDetails;
import cyclone.otusspring.library.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book createBook(String title, Integer year, long authorId, long genreId) {
        Book newBook = new Book(authorId, genreId, title, year);
        Book createdBook = bookDao.save(newBook);
        return createdBook;
    }

    @Override
    public List<BookDetails> findAllWithDetails() {
        return bookDao.findAllWithDetails();
    }
}
