package cyclone.otusspring.library.dao;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;

public class TestData {
    private TestData() {
    }


    public static final Author AUTHOR1 = new Author(1L, "Test Arthur", "Hailey", "Canada");
    public static final Author AUTHOR2 = new Author(2L, "Test Isaac", "Asimov", "Russia");
    public static final Author AUTHOR3 = new Author(3L, "Test Gabriel", "Marquez", "Argentina");

    public static final Author NEW_AUTHOR = new Author("New Author", "New Lastname", "New Homeland");



    public static final Book BOOK1 = new Book(1L, 1, 1, "Test Wheels", 1971);
    public static final Book BOOK2 = new Book(2L, 1, 1, "Test Airport", 1968);
    public static final Book BOOK3 = new Book(3L, 2, 2, "Test The End of Eternity", 1955);
    public static final Book BOOK4 = new Book(4L, 2, 2, "Test Foundation", 1951);
    public static final Book BOOK5 = new Book(5L, 3, 3, "Test 100 Years of Solitude", 1967);

    public static final Book NEW_BOOK = new Book(1, 1, "New Book", 2000);



    public static final Genre GENRE1 = new Genre(1L, "Test Adventures");
    public static final Genre GENRE2 = new Genre(2L, "Test Science fiction");
    public static final Genre GENRE3 = new Genre(3L, "Test Novel");
    public static final Genre GENRE4 = new Genre(4L, "Test Magic realism");

    public static final Genre NEW_GENRE = new Genre("New Genre");


    public static final long NO_SUCH_ID = 999;
}
