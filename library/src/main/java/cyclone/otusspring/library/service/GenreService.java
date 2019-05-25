package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findOne(String id);

    Genre create(String name);

    Genre save(Genre genre);

    void delete(String id);

}
