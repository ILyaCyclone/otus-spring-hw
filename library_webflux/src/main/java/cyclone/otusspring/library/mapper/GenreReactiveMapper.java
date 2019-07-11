package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenreReactiveMapper {

    private final GenreMapper genreMapper;

    public Mono<Genre> toGenre(Mono<GenreDto> genreDtoMono) {
        return genreDtoMono.map(genreMapper::toGenre);
    }

    public Mono<GenreDto> toGenreDto(Mono<Genre> genreMono) {
        return genreMono.map(genreMapper::toGenreDto);
    }

    public Flux<GenreDto> toGenreDtoFlux(Flux<Genre> genresFlux) {
        return genresFlux.map(genreMapper::toGenreDto);
    }

}
