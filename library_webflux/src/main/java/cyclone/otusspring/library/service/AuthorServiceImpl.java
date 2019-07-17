package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final BookService bookService;
    private final AuthorRepository authorRepository;

    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> findOne(String id) {
        return authorRepository.findOne(id);
    }

    @Override
    public Mono<Author> save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Mono<Void> delete(String id) {
        return findOne(id)
                .flatMap(author -> bookService.findByAuthor(author).collectList()
                        .flatMap(books -> books.isEmpty()
                                ? authorRepository.delete(author.getId())
                                : Mono.error(new DataIntegrityViolationException("Could not delete author." +
                                "\nReason: author has books. To delete author delete their books first.")))
                );
    }
}
