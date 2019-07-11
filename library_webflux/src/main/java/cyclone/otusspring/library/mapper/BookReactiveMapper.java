package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookReactiveMapper {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentMapper commentMapper;
    private final BookMapper bookMapper;



    public Mono<Book> toBook(Mono<BookDto> bookDtoMono) {
        return bookDtoMono.flatMap(bookDto -> {
            Mono<Author> authorMono = authorService.findOne(bookDto.getAuthorId());
            Mono<Genre> genreMono = genreService.findOne(bookDto.getGenreId());
            return Mono.zip(authorMono, genreMono)
                    .map(authorGenreTuple ->
                            new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), authorGenreTuple.getT1(), authorGenreTuple.getT2()));
        });

//        Mono<Author> authorMono = bookDtoMono.flatMap(bookDto -> authorService.findOne(bookDto.getAuthorId()));
//        Mono<Genre> genreMono = bookDtoMono.flatMap(bookDto -> genreService.findOne(bookDto.getGenreId()));
//        return Mono.zip(bookDtoMono, authorMono, genreMono)
//                .flatMap(tuple3 -> {
//                    BookDto bookDto = tuple3.getT1();
//                    Author author = tuple3.getT2();
//                    Genre genre = tuple3.getT3();
//                    return Mono.just(new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), author, genre));
//                });
    }

    public Mono<BookDto> toBookDto(Mono<Book> bookMono) {
        return bookMono.flatMap(book -> {
            List<CommentDto> commentDtoList = book.getComments().stream()
                    .map(comment -> commentMapper.toCommentDto(comment, book.getId()))
                    .collect(Collectors.toList());
            return Mono.just(new BookDto(book.getId(), book.getTitle(), book.getYear(), book.getAuthor().getId(), book.getGenre().getId(), commentDtoList));
        });
    }

    public Mono<BookListElementDto> toBooksElementDto(Mono<Book> bookMono) {
        return bookMono.map(book -> new BookListElementDto(book.getId(), book.getTitle(), book.getYear()
                , book.getAuthor().getFirstname(), book.getAuthor().getLastname(), book.getGenre().getName()));
    }


    public Flux<BookListElementDto> toBooksElementDtoFlux(Flux<Book> bookFlux) {
        return bookFlux.map(bookMapper::toBooksElementDto);
    }

    public Mono<BookWithoutComments> toBookWithoutComments(Mono<BookDto> bookDtoMono) {
        return bookDtoMono.flatMap(bookDto -> {
            Mono<Author> authorMono = authorService.findOne(bookDto.getAuthorId());
            Mono<Genre> genreMono = genreService.findOne(bookDto.getGenreId());
            return Mono.zip(authorMono, genreMono)
                    .map(authorGenreTuple ->
                            new BookWithoutComments(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), authorGenreTuple.getT1(), authorGenreTuple.getT2()));
        });
    }
}
