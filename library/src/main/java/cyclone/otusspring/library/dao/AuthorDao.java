package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> findAll();
    List<Author> findByName(String name);
    Author findOne(long id);
}
