package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.model.*;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookReactiveMapper {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentReactiveMapper commentMapper;
    private final BookMapper bookMapper;



    public Mono<Book> toBook(Mono<BookDto> bookDtoMono) {
        return bookDtoMono.flatMap(bookDto -> {
            Mono<Author> authorMono = authorService.findOne(bookDto.getAuthorId());
            Mono<Genre> genreMono = genreService.findOne(bookDto.getGenreId());
            Mono<List<Comment>> commentListMono = Flux.fromIterable(bookDto.getCommentDtoList()).transform(commentMapper::toCommentFlux).collectList();
            return Mono.zip(authorMono, genreMono, commentListMono)
                    .map(authorGenreCommentsTuple -> {
                        Book book = new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear()
                                , authorGenreCommentsTuple.getT1(), authorGenreCommentsTuple.getT2());
                        book.addComments(authorGenreCommentsTuple.getT3());
                        return book;
                    });
        });
    }

    public Mono<BookDto> toBookDto(Mono<Book> bookMono) {
        return bookMono.flatMap(book ->
                Flux.fromIterable(book.getComments())
                        .transform(commentFlux -> commentMapper.toCommentDtoFlux(commentFlux, book.getId()))
                        .collectList()
                        .map(commentDtoList -> new BookDto(book.getId(), book.getTitle(), book.getYear()
                                , book.getAuthor().getId(), book.getGenre().getId(), commentDtoList))
        );
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
