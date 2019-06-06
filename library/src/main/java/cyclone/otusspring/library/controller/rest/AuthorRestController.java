package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
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

import static cyclone.otusspring.library.controller.rest.AuthorRestController.BASE_URL;

/**
 * curl commands
 * ## get all authors
 * curl http://localhost:8080/api/v1/authors
 * <p>
 * ## get author by {id}
 * curl http://localhost:8080/api/v1/authors/{id}
 * <p>
 * ## create author
 * curl http://localhost:8080/api/v1/authors -X POST -H "Content-Type:application/json" -d "{\"firstname\":\"New Fi
 * rstname\", \"lastname\": \"New Lastname\", \"homeland\": \"New Homeland\"}" -i
 * <p>
 * ## update author by {id}
 * curl http://localhost:8080/api/v1/authors/{id} -X PUT -H "Content-Type:application/json" -d "{\"firstname\":\"New F
 * irstname\", \"lastname\": \"New Lastname\", \"homeland\": \"upd\"}" -i
 * <p>
 * ## delete author by {id}
 * curl http://localhost:8080/api/v1/authors/{id} -X DELETE -i
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class AuthorRestController {
    static final String BASE_URL = "/api/v1/authors";
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;



    @GetMapping
    public List<AuthorDto> findAll() {
        return authorMapper.toAuthorDtoList(authorService.findAll());
    }



    @GetMapping("/{id}")
    public AuthorDto findOne(@PathVariable("id") String id) {
        return authorMapper.toAuthorDto(authorService.findOne(id));
    }



    @PostMapping
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        if (authorDto.getId() != null && authorDto.getId().length() == 0) {
            authorDto.setId(null);
        }
        Author savedAuthor = authorService.save(authorMapper.toAuthor(authorDto));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BASE_URL + "/{id}")
                .buildAndExpand(savedAuthor.getId()).toUri();
        AuthorDto savedDto = authorMapper.toAuthorDto(savedAuthor);

        return ResponseEntity.created(uriOfNewResource).body(savedDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody AuthorDto authorDto, @PathVariable("id") String id) {
        authorDto.setId(id);
        authorService.save(authorMapper.toAuthor(authorDto));
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") String id) {
        authorService.delete(id);
    }
}
