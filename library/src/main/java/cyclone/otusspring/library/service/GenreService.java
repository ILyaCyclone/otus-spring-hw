package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findOne(String id);

    Genre save(Genre genre);

    Genre save(GenreDto genreDto);

    void delete(String id);

}
