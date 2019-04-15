package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface GenreService {
    Genre create(String name);

    List<Genre> findAll();
}
