package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
//        return authorRepository.findAll();
        return Collections.emptyList();
    }

    @Override
    public Author findOne(String id) {
//        return authorRepository.findOne(id);
        return new Author("99");
    }

    @Override
    public Author save(Author author) {
//        return authorRepository.save(author);
        return new Author("99");
    }

    @Override
    public void delete(String id) {
        authorRepository.delete(id);
    }
}
