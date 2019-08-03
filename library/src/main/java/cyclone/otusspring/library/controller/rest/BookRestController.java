package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.AuthenticationService;
import cyclone.otusspring.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

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

    private final AuthenticationService authenticationService;



    @GetMapping
    public List<BookListElementDto> findAll() {
        return bookMapper.toBooksElementDtoList(bookService.findAll());
    }



    @GetMapping("/{id}")
    public BookDto findOne(@PathVariable("id") String id) {
        return bookMapper.toBookDto(bookService.findOne(id));
    }


    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        if (bookDto.getId() != null && "".equals(bookDto.getId())) {
            bookDto.setId(null);
        }
        Book savedBook = bookService.save(bookMapper.toBook(bookDto));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BASE_URL + "/{id}")
                .buildAndExpand(savedBook.getId()).toUri();
        BookDto savedDto = bookMapper.toBookDto(savedBook);

        return ResponseEntity.created(uriOfNewResource).body(savedDto);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody BookDto bookDto, @PathVariable("id") String id) {
        bookDto.setId(id);
        bookService.save(bookMapper.toBook(bookDto));
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") String id) {
        bookService.delete(id);
    }



    @PostMapping("/{id}/comments/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveComment(@PathVariable(name = "id") String bookId, @RequestBody CommentDto commentDto) {
        Book book = bookService.findOne(bookId);

        commentDto.setCommentator(authenticationService.getCurrentUsername());
        if (commentDto.getDate() == null) {
            commentDto.setDate(LocalDateTime.now());
        }

        Comment comment = commentMapper.toComment(commentDto);
        book.addComment(comment);
        bookService.save(book);
    }
}
