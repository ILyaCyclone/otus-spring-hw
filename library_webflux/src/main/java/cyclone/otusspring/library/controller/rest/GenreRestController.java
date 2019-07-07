package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(GenreRestController.class);

    private final GenreService genreService;
    private final GenreMapper genreMapper;



    @GetMapping
    public Flux<GenreDto> findAll() {
        return genreService.findAll()
                .map(genreMapper::toGenreDto);
    }



    @GetMapping("/{id}")
    public Mono<GenreDto> findOne(@PathVariable("id") String id) {
        return genreService.findOne(id)
                .map(genreMapper::toGenreDto);
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GenreDto> create(@RequestBody GenreDto genreDto) {
        if (genreDto.getId() != null && genreDto.getId().length() == 0) {
            genreDto.setId(null);
        }
        return genreService.save(genreMapper.toGenre(genreDto))
                .map(genreMapper::toGenreDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody GenreDto genreDto, @PathVariable("id") String id) {
        genreDto.setId(id);
        genreService.save(genreMapper.toGenre(genreDto));
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") String id) {
        genreService.delete(id);
    }
}
