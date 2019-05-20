package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author create(AuthorDto authorDto) {
        Author authorToCreate = new Author(authorDto.getFirstname(), authorDto.getLastname(), authorDto.getHomeland());
        return authorRepository.save(authorToCreate);
    }
}
