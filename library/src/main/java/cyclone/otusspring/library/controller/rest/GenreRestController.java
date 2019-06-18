package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static cyclone.otusspring.library.controller.rest.GenreRestController.BASE_URL;

/**
 * curl commands
 * ## get all genres
 * curl http://localhost:8080/api/v1/genres
 * <p>
 * ## get genre by {id}
 * curl http://localhost:8080/api/v1/genres/{id}
 * <p>
 * ## create genre
 * curl http://localhost:8080/api/v1/genres -X POST -H "Content-Type:application/json" -d "{\"firstname\":\"New Fi
 * rstname\", \"lastname\": \"New Lastname\", \"homeland\": \"New Homeland\"}" -i
 * <p>
 * ## update genre by {id}
 * curl http://localhost:8080/api/v1/genres/{id} -X PUT -H "Content-Type:application/json" -d "{\"firstname\":\"New F
 * irstname\", \"lastname\": \"New Lastname\", \"homeland\": \"upd\"}" -i
 * <p>
 * ## delete genre by {id}
 * curl http://localhost:8080/api/v1/genres/{id} -X DELETE -i
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class GenreRestController {
    static final String BASE_URL = "/api/v1/genres";
    private static final Logger logger = LoggerFactory.getLogger(GenreRestController.class);

    private final GenreService genreService;
    private final GenreMapper genreMapper;



    @GetMapping
    public List<GenreDto> findAll() {
        return genreMapper.toGenreDtoList(genreService.findAll());
    }



    @GetMapping("/{id}")
    public GenreDto findOne(@PathVariable("id") String id) {
        return genreMapper.toGenreDto(genreService.findOne(id));
    }



    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody GenreDto genreDto) {
        if (genreDto.getId() != null && genreDto.getId().length() == 0) {
            genreDto.setId(null);
        }
        Genre savedGenre = genreService.save(genreMapper.toGenre(genreDto));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BASE_URL + "/{id}")
                .buildAndExpand(savedGenre.getId()).toUri();
        GenreDto savedDto = genreMapper.toGenreDto(savedGenre);

        return ResponseEntity.created(uriOfNewResource).body(savedDto);
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
