package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findOne(String id) {
        return authorRepository.findOne(id);
    }

    @Override
    public Author save(AuthorDto authorDto) {
        return authorRepository.save(authorMapper.toAuthor(authorDto));
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void delete(String id) {
        authorRepository.delete(id);
    }
}
