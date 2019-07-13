package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final BookService bookService;
    private final GenreRepository genreRepository;

    @Override
    public Mono<Genre> findOne(String id) {
        return genreRepository.findOne(id);
    }

    @Override
    public Mono<Genre> save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Mono<Void> delete(String id) {
        return findOne(id)
                .flatMap(genre -> bookService.findByGenre(genre).collectList()
                        .flatMap(books -> books.isEmpty()
                                ? genreRepository.delete(genre.getId())
                                : Mono.error(new DataIntegrityViolationException("Could not delete genre." +
                                "\nReason: genre has books. To delete genre delete its books first.")))
                );
    }
}
