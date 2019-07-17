package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.dto.BookFormDto;
import cyclone.otusspring.library.dto.GenreDto;
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
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<BookFormDto> getBookFormData(@PathVariable("id") String id) {

        Mono<BookFormDto> bookDtoMono = "new".equals(id)
                ? Mono.just(new BookFormDto())
                : bookService.findOne(id).transform(bookMapper::toBookDto)
                    .map(bookDto -> {
                        BookFormDto bookFormDto = new BookFormDto();
                        bookFormDto.setBookDto(bookDto);
                        return bookFormDto;
                    });

        return Mono.zip(bookDtoMono
                , authorService.findAll().transform(authorMapper::toAuthorDtoFlux).collectList()
                , genreService.findAll().transform(genreMapper::toGenreDtoFlux).collectList()
        ).map(tuple3 -> {
            BookFormDto bookFormDto = tuple3.getT1();
            List<AuthorDto> authorDtoList = tuple3.getT2();
            List<GenreDto> genreDtoList = tuple3.getT3();

            bookFormDto.setAllAuthors(authorDtoList);
            bookFormDto.setAllGenres(genreDtoList);
            return bookFormDto;
        });
    }

}
