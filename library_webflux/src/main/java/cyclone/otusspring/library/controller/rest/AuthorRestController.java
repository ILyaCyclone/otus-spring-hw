package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorReactiveMapper;
import cyclone.otusspring.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cyclone.otusspring.library.controller.rest.AuthorRestController.BASE_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class AuthorRestController {
    static final String BASE_URL = "/api/v1/authors";

    private final AuthorService authorService;
    private final AuthorReactiveMapper authorMapper;



    @GetMapping
    public Flux<AuthorDto> findAll() {
        return authorService.findAll()
                .transform(authorMapper::toAuthorDtoFlux);
    }



    @GetMapping("/{id}")
    public Mono<AuthorDto> findOne(@PathVariable("id") String id) {
        return authorService.findOne(id)
                .transform(authorMapper::toAuthorDto);
    }



    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        return Mono.just(authorDto)
                .doOnNext(aDto -> {
                    if (aDto.getId() != null && aDto.getId().length() == 0) {
                        aDto.setId(null);
                    }
                })
                .transform(authorMapper::toAuthor)
                .flatMap(authorService::save)
                .transform(authorMapper::toAuthorDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody AuthorDto authorDto, @PathVariable("id") String id) {
        return Mono.just(authorDto)
                .doOnNext(aDto -> aDto.setId(id))
                .transform(authorMapper::toAuthor)
                .flatMap(authorService::save)
                .then();
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return authorService.delete(id);
    }
}
