package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();

    List<Genre> findByName(String name);

    Genre findOne(String id);

    Genre save(Genre genre);

    void delete(String id);

    void delete(Genre genre);

    boolean exists(String id);
}
