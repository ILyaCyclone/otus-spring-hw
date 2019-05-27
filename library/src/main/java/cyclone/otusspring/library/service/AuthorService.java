package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author findOne(String id);

    Author save(AuthorDto authorDto);

    Author save(Author author);

    void delete(String id);
}
