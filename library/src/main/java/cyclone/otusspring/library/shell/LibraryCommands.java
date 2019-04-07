package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.BookDetails;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.StringJoiner;

@ShellComponent
public class LibraryCommands {
    private static final Logger logger = LoggerFactory.getLogger(LibraryCommands.class);

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;

    public LibraryCommands(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ShellMethod(value = "List all books")
    public String listBooks() {
        List<BookDetails> booksWithDetails = bookService.findAllWithDetails();

        StringJoiner joiner = new StringJoiner(";\n");
        booksWithDetails.forEach(bookDetails ->
                joiner.add(String.format("#%d \"%s\" by %s %s, %s", bookDetails.getBookId(), bookDetails.getTitle(), bookDetails.getAuthor()
                        .getFirstname(), bookDetails.getAuthor().getLastname(), bookDetails.getGenre().getName())
                        + (bookDetails.getYear() != null ? String.format(" (%d)", bookDetails.getYear()) : ""))
        );
        return joiner.toString();
    }

    @ShellMethod(value = "Create book")
    public String createBook(
            @ShellOption String title
            , @ShellOption(defaultValue = ShellOption.NULL, help = "publish year") Integer year
            , @ShellOption long authorId
            , @ShellOption long genreId) {
        logger.info("create book title: {}, year: {}, authorId: {}, genreId: {}", title, year, authorId, genreId);

        Book createdBook = bookService.createBook(title, year, authorId, genreId);
        return "created book #" + createdBook.getBookId() + " \"" + createdBook.getTitle() + "\"";
    }

    @ShellMethod(value = "List all authors")
    public String listAuthors() {
        List<Author> authors = authorService.findAll();

        StringJoiner joiner = new StringJoiner(";\n");
        authors.forEach(author ->
                joiner.add("#" + author.getAuthorId() + " " + author.getFirstname() + " " + author.getLastname()
                        + (author.getHomeland() != null ? ", " + author.getHomeland() : ""))
        );
        return joiner.toString();
    }

    @ShellMethod(value = "List all genres")
    public String listGenres() {
        List<Genre> genres = genreService.findAll();

        StringJoiner joiner = new StringJoiner(";\n");
        genres.forEach(genre -> joiner.add("#" + genre.getGenreId() + " " + genre.getName()));
        return joiner.toString();
    }
}
