package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreReactiveMapper;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cyclone.otusspring.library.controller.rest.GenreRestController.BASE_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class GenreRestController {
    static final String BASE_URL = "/api/v1/genres";

    private final GenreService genreService;
    private final GenreReactiveMapper genreMapper;



    @GetMapping
    public Flux<GenreDto> findAll() {
        return genreService.findAll()
                .transform(genreMapper::toGenreDtoFlux);
    }



    @GetMapping("/{id}")
    public Mono<GenreDto> findOne(@PathVariable("id") String id) {
        return genreService.findOne(id)
                .transform(genreMapper::toGenreDto);
    }



    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<GenreDto> create(@RequestBody GenreDto genreDto) {
        return Mono.just(genreDto)
                .doOnNext(aDto -> {
                    if (aDto.getId() != null && aDto.getId().length() == 0) {
                        aDto.setId(null);
                    }
                })
                .transform(genreMapper::toGenre)
                .flatMap(genreService::save)
                .transform(genreMapper::toGenreDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody GenreDto genreDto, @PathVariable("id") String id) {
        return Mono.just(genreDto)
                .doOnNext(aDto -> aDto.setId(id))
                .transform(genreMapper::toGenre)
                .flatMap(genreService::save)
                .then();
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return genreService.delete(id);
    }
}
