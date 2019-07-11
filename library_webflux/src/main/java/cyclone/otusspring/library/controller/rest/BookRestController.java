package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.BookReactiveMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cyclone.otusspring.library.controller.rest.BookRestController.BASE_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class BookRestController {
    static final String BASE_URL = "/api/v1/books";
    private static final Logger logger = LoggerFactory.getLogger(BookRestController.class);

    private final BookService bookService;
    private final BookReactiveMapper bookMapper;

    private final CommentMapper commentMapper;



    @GetMapping
    public Flux<BookListElementDto> findAll() {
        return bookService.findAll()
                .transform(bookMapper::toBooksElementDtoFlux);
    }



    @GetMapping("/{id}")
    public Mono<BookDto> findOne(@PathVariable("id") String id) {
        return bookService.findOne(id)
                .transform(bookMapper::toBookDto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@RequestBody BookDto bookDto) {
        return Mono.just(bookDto)
                .doOnNext(bDto -> {
                    if (bDto.getId() != null && "".equals(bDto.getId())) {
                        bDto.setId(null);
                    }
                })
                .transform(bookMapper::toBook)
                .flatMap(bookService::save)
                .transform(bookMapper::toBookDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody BookDto bookDto, @PathVariable("id") String id) {
        return Mono.just(bookDto)
                .doOnNext(bDto -> bDto.setId(id))
                .transform(bookMapper::toBook)
                .flatMap(bookService::save)
                .then();
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return bookService.delete(id);
    }



    @PostMapping("/{id}/comments/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Void> saveComment(@PathVariable(name = "id") String bookId, @RequestBody CommentDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);
        return bookService.findOne(bookId)
                .doOnNext(book -> book.addComment(comment))
                .flatMap(bookService::save)
                .then();
    }
}
