package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book save(BookDto bookDto) {
        try {
            return bookRepository.save(bookMapper.toBook(bookDto));
        } catch (Exception e) {
            throw new RuntimeException("Could not save book", e);
        }
    }

    @Override
    public void delete(String id) {
        bookRepository.delete(id);
    }
}
