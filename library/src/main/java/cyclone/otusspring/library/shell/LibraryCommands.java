package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.dto.BookDto;
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
import org.springframework.shell.table.Table;

import java.util.List;

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
    public Table listBooks(
            @ShellOption(help = "output full info") boolean verbose
    ) {
        List<Book> books = bookService.findAll();

        SimpleTableBuilder tableBuilder = new SimpleTableBuilder()
                .data(books)
                .addHeader("id", "ID")
                .addHeader("title", "Title")
                .addHeader("year", "Year");
        if (!verbose) {
            return tableBuilder
                    .addHeader("author.lastname", "Author")
                    .addHeader("genre.name", "Genre")
                    .build();
        } else {
            return tableBuilder
                    .addHeader("author.id", "Author ID")
                    .addHeader("author.firstname", "Author First Name")
                    .addHeader("author.lastname", "Author Last Name")
                    .addHeader("author.homeland", "Author Homeland")

                    .addHeader("genre.id", "Genre ID")
                    .addHeader("genre.name", "Genre Name")
                    .build();
        }
    }

    @ShellMethod(value = "Create book")
    public String createBook(
            @ShellOption String title
            , @ShellOption(defaultValue = ShellOption.NULL, help = "publish year") Integer year
            , @ShellOption String authorId
            , @ShellOption String genreId) {
        logger.info("create book title: {}, year: {}, authorId: {}, genreId: {}", title, year, authorId, genreId);



        Book createdBook = bookService.create(new BookDto(title, year, authorId, genreId));
        return "Book \"" + createdBook.getTitle() + "\" created successfully with ID " + createdBook.getId();
    }

    @ShellMethod(value = "Create author")
    public String createAuthor(
            @ShellOption String firstname
            , @ShellOption String lastname
            , @ShellOption(defaultValue = ShellOption.NULL) String homeland) {
        Author createdAuthor = authorService.create(new AuthorDto(firstname, lastname, homeland));
        return "Author \"" + createdAuthor.getFirstname() + " " + createdAuthor.getLastname()
                + "\" created successfully with ID " + createdAuthor.getId();
    }

    @ShellMethod(value = "Create author")
    public String createGenre(
            @ShellOption String name) {
        Genre createdGenre = genreService.create(name);
        return "Genre \"" + createdGenre.getName() + "\" created successfully with ID " + createdGenre.getId();
    }

    @ShellMethod(value = "List all authors")
    public Table listAuthors() {
        List<Author> authors = authorService.findAll();

        return new SimpleTableBuilder()
                .data(authors)
                .addHeader("id", "ID")
                .addHeader("firstname", "First Name")
                .addHeader("lastname", "Last Name")
                .addHeader("homeland", "Homeland")
                .build();
    }

    @ShellMethod(value = "List all genres")
    public Table listGenres() {
        List<Genre> genres = genreService.findAll();

        return new SimpleTableBuilder()
                .data(genres)
                .addHeader("id", "ID")
                .addHeader("name", "Genre")
                .build();
    }


}
