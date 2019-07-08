package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookFormDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cyclone.otusspring.library.controller.rest.BookFormdataRestController.BASE_URL;


@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class BookFormdataRestController {
    static final String BASE_URL = "/api/v1/books/formdata";
    private static final Logger logger = LoggerFactory.getLogger(BookFormdataRestController.class);

    private final BookService bookService;
    private final BookMapper bookMapper;

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    private final GenreService genreService;
    private final GenreMapper genreMapper;



    @GetMapping("/{id}")
    public BookFormDto getBookFormData(@PathVariable("id") String id) {
        BookFormDto bookFormDto = new BookFormDto();
        if (!"new".equals(id)) {
            //TODO unblock
            bookFormDto.setBookDto(bookMapper.toBookDto(bookService.findOne(id).block()));
        }
        //TODO unblock
        bookFormDto.setAllAuthors(authorMapper.toAuthorDtoList(authorService.findAll().collectList().block()));
        //TODO unblock
        bookFormDto.setAllGenres(genreMapper.toGenreDtoList(genreService.findAll().collectList().block()));
        return bookFormDto;
    }

}
