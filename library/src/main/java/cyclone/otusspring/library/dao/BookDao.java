package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    List<Book> findByTitle(String title);

    Book findOne(long id);

    Book save(Book book);

    void delete(long id);

    void delete(Book book);

    boolean exists(long id);
}
