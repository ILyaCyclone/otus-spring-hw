package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

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
        return genreRepository.delete(id);
    }
}
