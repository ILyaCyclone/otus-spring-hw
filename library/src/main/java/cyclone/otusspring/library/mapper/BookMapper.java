package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentMapper commentMapper;

    public Book toBook(BookDto bookDto) {
        Author author = authorService.findOne(bookDto.getAuthorId());
        Genre genre = genreService.findOne(bookDto.getGenreId());
        Book book = new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), author, genre);
        book.addComments(
                bookDto.getCommentDtoList().stream()
                        .map(commentMapper::toComment)
                        .collect(Collectors.toList())
        );

        return book;
    }

    public BookDto toDto(Book book) {
        List<CommentDto> commentDtoList = book.getComments().stream()
                .map(comment -> commentMapper.toCommentDto(comment, book.getId()))
                .collect(Collectors.toList());
        return new BookDto(book.getId(), book.getTitle(), book.getYear(), book.getAuthor().getId(), book.getGenre().getId(), commentDtoList);
    }
}
