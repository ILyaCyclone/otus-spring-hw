package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.BookMapper;
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
    private final BookMapper bookMapper;

    private final CommentMapper commentMapper;



    @GetMapping
    public Flux<BookListElementDto> findAll() {
        return bookService.findAll()
                .map(bookMapper::toBooksElementDto);
    }



    @GetMapping("/{id}")
    public Mono<BookDto> findOne(@PathVariable("id") String id) {
        return bookService.findOne(id)
                .map(bookMapper::toBookDto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@RequestBody BookDto bookDto) {
        if (bookDto.getId() != null && "".equals(bookDto.getId())) {
            bookDto.setId(null);
        }
        return bookService.save(bookMapper.toBook(bookDto))
                .map(bookMapper::toBookDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody BookDto bookDto, @PathVariable("id") String id) {
        bookDto.setId(id);
        return bookService.save(bookMapper.toBook(bookDto))
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
//                .map(book -> {book.addComment(comment); return book;})
                .doOnNext(book -> book.addComment(comment))
                .flatMap(bookService::save)
                .then();
    }
}
