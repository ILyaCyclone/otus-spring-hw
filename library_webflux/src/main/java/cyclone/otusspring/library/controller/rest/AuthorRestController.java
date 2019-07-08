package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;



    @GetMapping
    public Flux<AuthorDto> findAll() {
        return authorService.findAll()
                .map(authorMapper::toAuthorDto);
    }



    @GetMapping("/{id}")
    public Mono<AuthorDto> findOne(@PathVariable("id") String id) {
        return authorService.findOne(id)
                .map(authorMapper::toAuthorDto);
    }



    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        if (authorDto.getId() != null && authorDto.getId().length() == 0) {
            authorDto.setId(null);
        }
        return authorService.save(authorMapper.toAuthor(authorDto))
                .map(authorMapper::toAuthorDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody AuthorDto authorDto, @PathVariable("id") String id) {
        authorDto.setId(id);
        return authorService.save(authorMapper.toAuthor(authorDto))
                .then();
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return authorService.delete(id);
    }
}
