package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> findAll();

    List<Author> findByName(String name);

    Author findOne(String id);

    Author save(Author author);

    void delete(String id);

    void delete(Author author);

    boolean exists(String id);
}
