package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.BookFormDto;
import cyclone.otusspring.library.mapper.AuthorReactiveMapper;
import cyclone.otusspring.library.mapper.BookReactiveMapper;
import cyclone.otusspring.library.mapper.GenreReactiveMapper;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
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

    private final BookService bookService;
    private final BookReactiveMapper bookMapper;

    private final AuthorService authorService;
    private final AuthorReactiveMapper authorMapper;

    private final GenreService genreService;
    private final GenreReactiveMapper genreMapper;



    @GetMapping("/{id}")
    public BookFormDto getBookFormData(@PathVariable("id") String id) {
        BookFormDto bookFormDto = new BookFormDto();
        if (!"new".equals(id)) {
            bookFormDto.setBookDto(bookService.findOne(id)
                    .transform(bookMapper::toBookDto)
                    .block());
        }
        bookFormDto.setAllAuthors(authorService.findAll()
                .transform(authorMapper::toAuthorDtoFlux)
                .collectList().block());
        bookFormDto.setAllGenres(genreService.findAll()
                .transform(genreMapper::toGenreDtoFlux)
                .collectList().block());
        return bookFormDto;

//        return Mono.zip(authorService.findAll().collectList(), genreService.findAll().collectList())
//                .map(tuple2 -> {
//                    BookFormDto bookFormDto = new BookFormDto();
//                    bookFormDto.setAllAuthors(authorMapper.toAuthorDtoList(tuple2.getT1()));
//                    bookFormDto.setAllGenres(genreMapper.toGenreDtoList(tuple2.getT2()));
//                    if (!"new".equals(id)) {
//                        bookFormDto.setBookDto(bookMapper.toBookDto(bookService.findOne(id).block()));
//                    }
//                    return bookFormDto;
//                });
    }

}
