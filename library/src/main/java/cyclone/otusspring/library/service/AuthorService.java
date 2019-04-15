package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;

import java.util.List;

public interface AuthorService {
    Author create(AuthorDto authorDto);

    List<Author> findAll();
}
