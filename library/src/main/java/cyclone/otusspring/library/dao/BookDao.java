package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    List<Book> findByTitle(String title);

    Book findOne(long id);
}
