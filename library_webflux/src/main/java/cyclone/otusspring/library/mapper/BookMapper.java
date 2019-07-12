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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentMapper commentMapper;

    public Book toBook(BookDto bookDto) {
        try {
            Author author = authorService.findOne(bookDto.getAuthorId()).block();
            Genre genre = genreService.findOne(bookDto.getGenreId()).block();
            Book book = new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), author, genre);
            book.addComments(
                    bookDto.getCommentDtoList().stream()
                            .map(commentMapper::toComment)
                            .collect(Collectors.toList())
            );

            return book;
        } catch (Exception e) {
            throw new RuntimeException("Could not map to book, reason: " + e.getMessage(), e);
        }
    }

    public BookDto toBookDto(Book book) {
        List<CommentDto> commentDtoList = book.getComments().stream()
                .map(comment -> commentMapper.toCommentDto(comment, book.getId()))
                .collect(Collectors.toList());
        return new BookDto(book.getId(), book.getTitle(), book.getYear(), book.getAuthor().getId(), book.getGenre().getId(), commentDtoList);
    }


    public BookListElementDto toBooksElementDto(Book book) {
        return new BookListElementDto(book.getId(), book.getTitle(), book.getYear()
                , book.getAuthor().getFirstname(), book.getAuthor().getLastname(), book.getGenre().getName());
    }


    public List<BookListElementDto> toBooksElementDtoList(Collection<Book> books) {
        return books.stream()
                .map(this::toBooksElementDto)
                .collect(Collectors.toList());
    }

    public BookWithoutComments toBookWithoutComments(BookDto bookDto) {
        try {
            Author author = authorService.findOne(bookDto.getAuthorId()).block();
            Genre genre = genreService.findOne(bookDto.getGenreId()).block();
            return new BookWithoutComments(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), author, genre);
        } catch (Exception e) {
            throw new RuntimeException("Could not map to book, reason: " + e.getMessage(), e);
        }
    }

}
