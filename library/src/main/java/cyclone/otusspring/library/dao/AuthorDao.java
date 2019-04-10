package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> findAll();

    List<Author> findByName(String name);

    Author findOne(long id);

    Author save(Author author);

    void delete(long id);

    void delete(Author author);

    boolean exists(long id);
}
