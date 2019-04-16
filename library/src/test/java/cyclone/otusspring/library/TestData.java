package cyclone.otusspring.library;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.model.Genre;

import java.time.LocalDateTime;

public class TestData {
    private TestData() {
    }


    public static final Author AUTHOR1 = new Author(1L, "Test Arthur", "Hailey", "Canada");
    public static final Author AUTHOR2 = new Author(2L, "Test Isaac", "Asimov", "Russia");
    public static final Author AUTHOR3 = new Author(3L, "Test Gabriel", "Marquez", "Argentina");

    public static final Author NEW_AUTHOR = new Author("New Author", "New Lastname", "New Homeland");



    public static final Genre GENRE1 = new Genre(1L, "Test Adventures");
    public static final Genre GENRE2 = new Genre(2L, "Test Science fiction");
    public static final Genre GENRE3 = new Genre(3L, "Test Novel");
    public static final Genre GENRE4 = new Genre(4L, "Test Magic realism");

    public static final Genre NEW_GENRE = new Genre("New Genre");



    public static final Book BOOK1 = new Book(1L, "Test Wheels", 1971, AUTHOR1, GENRE1);
    public static final Book BOOK2 = new Book(2L, "Test Airport", 1968, AUTHOR1, GENRE1);
    public static final Book BOOK3 = new Book(3L, "Test The End of Eternity", 1955, AUTHOR2, GENRE2);
    public static final Book BOOK4 = new Book(4L, "Test Foundation", 1951, AUTHOR2, GENRE2);
    public static final Book BOOK5 = new Book(5L, "Test 100 Years of Solitude", 1967, AUTHOR3, GENRE3);

    public static final Book NEW_BOOK = new Book("New Book", 2000, AUTHOR1, GENRE1);



    public static final Comment COMMENT1 = new Comment(1L, "Test Commentator 1", "Test comment 1"
            , LocalDateTime.of(2019, 1, 1, 10, 00, 00), BOOK1);
    public static final Comment COMMENT2 = new Comment(2L, "Test Commentator 1", "Test comment 2"
            , LocalDateTime.of(2019, 2, 2, 10, 00, 00), BOOK2);
    public static final Comment COMMENT3 = new Comment(3L, "Test Commentator 2", "Test comment 3"
            , LocalDateTime.of(2019, 3, 3, 10, 00, 00), BOOK1);
    public static final Comment COMMENT4 = new Comment(4L, "Test Commentator 2", "Test comment 4"
            , LocalDateTime.of(2019, 4, 4, 10, 00, 00), BOOK2);
    public static final Comment COMMENT5 = new Comment(5L, "Test Commentator 3", "Test comment 5"
            , LocalDateTime.of(2019, 5, 5, 10, 00, 00), BOOK3);

    public static final Comment NEW_COMMENT = new Comment("New Commentator", "test new comment"
            , LocalDateTime.of(2015, 1, 1, 15, 00, 00), BOOK3);



    public static final long NO_SUCH_ID = 999;
}
