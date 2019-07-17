package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthorReactiveMapper {

    private final AuthorMapper authorMapper;

    public Mono<Author> toAuthor(Mono<AuthorDto> authorDtoMono) {
        return authorDtoMono.map(authorMapper::toAuthor);
    }

    public Mono<AuthorDto> toAuthorDto(Mono<Author> authorMono) {
        return authorMono.map(authorMapper::toAuthorDto);
    }

    public Flux<AuthorDto> toAuthorDtoFlux(Flux<Author> authorsFlux) {
        return authorsFlux.map(authorMapper::toAuthorDto);
    }

}
