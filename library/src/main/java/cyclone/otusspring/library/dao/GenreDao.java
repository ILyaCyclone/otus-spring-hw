package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> findAll();

    List<Genre> findByName(String name);

    Genre findOne(long id);
}