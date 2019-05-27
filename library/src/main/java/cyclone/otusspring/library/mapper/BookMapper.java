package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.GenreService;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookMapper(AuthorService authorService, GenreService genreService) {
        this.authorService = authorService;
        this.genreService = genreService;
    }


    public Book toBook(BookDto bookDto) {
        Author author = authorService.findOne(bookDto.getAuthorId());
        Genre genre = genreService.findOne(bookDto.getGenreId());

        return new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getYear(), author, genre);
    }

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getYear(), book.getAuthor().getId(), book.getGenre().getId());
    }
}
