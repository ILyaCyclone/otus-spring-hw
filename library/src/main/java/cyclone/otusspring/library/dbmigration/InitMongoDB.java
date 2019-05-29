package cyclone.otusspring.library.dbmigration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;

@ChangeLog(order = "001")
public class InitMongoDB {

    private Author author1;
    private Author author2;
    private Author author3;

    private Genre genre1;
    private Genre genre2;
    private Genre genre3;

    @ChangeSet(order = "000", id = "dropDB", author = "cyclone", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "cyclone", runAlways = true)
    public void initAuthors(MongoTemplate template) {
        author1 = template.save(new Author("1", "Arthur", "Hailey", "Canada"));
        author2 = template.save(new Author("2", "Isaac", "Asimov", "Russia"));
        author3 = template.save(new Author("3", "Gabriel", "Marquez", "Argentina"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "cyclone", runAlways = true)
    public void initGenres(MongoTemplate template) {
        genre1 = template.save(new Genre("1", "Adventures"));
        genre2 = template.save(new Genre("2", "Science fiction"));
        genre3 = template.save(new Genre("3", "Novel"));
        template.save(new Genre("4", "Magic realism"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "cyclone", runAlways = true)
    public void initBooks(MongoTemplate template) {
        Book book1 = new Book("1", "Wheels", 1971, author1, genre1);
        book1.addComment(new Comment("Commentator 1", "exciting!", LocalDateTime.of(2019, 5, 29, 10, 0)));
        book1.addComment(new Comment("Commentator 2", "breathtaking experience", LocalDateTime.of(2019, 5, 29, 11, 0)));
        template.save(book1);
        template.save(new Book("2", "Airport", 1968, author1, genre1));
        template.save(new Book("3", "The End of Eternity", 1955, author2, genre2));
        template.save(new Book("4", "Foundation", 1951, author2, genre2));
        template.save(new Book("5", "100 Years of Solitude", 1967, author3, genre3));
    }
}