package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);

    Book findOne(String id);

    Book save(Book book);

    BookWithoutComments save(BookWithoutComments book);

    void delete(String id);

    void delete(Book book);

    boolean exists(String id);

    long count();
}
