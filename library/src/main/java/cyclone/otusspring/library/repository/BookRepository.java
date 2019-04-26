package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    List<Book> findByTitle(String title);

    Book findOne(long id);

    Book save(Book book);

    void delete(long id);

    void delete(Book book);

    boolean exists(long id);
}
