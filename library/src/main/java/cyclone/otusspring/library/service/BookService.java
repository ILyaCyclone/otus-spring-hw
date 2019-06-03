package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface BookService {
    Book findOne(String bookId);

    List<Book> findAll();

    Book save(Book book);

    void delete(String id);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);
}