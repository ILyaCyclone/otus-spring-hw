package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cyclone.otusspring.library.controller.rest.BookRestController.BASE_URL;

/**
 * curl commands
 * ## get all books
 * curl http://localhost:8080/api/v1/books
 * <p>
 * ## get book by {id}
 * curl http://localhost:8080/api/v1/books/{id}
 * <p>
 * ## create book
 * curl http://localhost:8080/api/v1/books -X POST -H "Content-Type:application/json" -d "{\"firstname\":\"New Fi
 * rstname\", \"lastname\": \"New Lastname\", \"homeland\": \"New Homeland\"}" -i
 * <p>
 * ## update book by {id}
 * curl http://localhost:8080/api/v1/books/{id} -X PUT -H "Content-Type:application/json" -d "{\"firstname\":\"New F
 * irstname\", \"lastname\": \"New Lastname\", \"homeland\": \"upd\"}" -i
 * <p>
 * ## delete book by {id}
 * curl http://localhost:8080/api/v1/books/{id} -X DELETE -i
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class BookRestController {
    static final String BASE_URL = "/api/v1/books";
    private static final Logger logger = LoggerFactory.getLogger(BookRestController.class);

    private final BookService bookService;
    private final BookMapper bookMapper;



    @GetMapping
    public List<BookListElementDto> findAll() {
        return bookMapper.toBooksElementDtoList(bookService.findAll());
    }



    @GetMapping("/{id}")
    public BookDto findOne(@PathVariable("id") String id) {
        return bookMapper.toDto(bookService.findOne(id));
    }



//
//    @PostMapping
//    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
//        if (bookDto.getId() != null && bookDto.getId().length() == 0) {
//            bookDto.setId(null);
//        }
//        Book savedBook = bookService.save(bookMapper.toBook(bookDto));
//
//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(BASE_URL + "/{id}")
//                .buildAndExpand(savedBook.getId()).toUri();
//        BookDto savedDto = bookMapper.toBookDto(savedBook);
//
//        return ResponseEntity.created(uriOfNewResource).body(savedDto);
//    }
//
//
//
//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public void update(@RequestBody BookDto bookDto, @PathVariable("id") String id) {
//        bookDto.setId(id);
//        bookService.save(bookMapper.toBook(bookDto));
//    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") String id) {
        bookService.delete(id);
    }
}
