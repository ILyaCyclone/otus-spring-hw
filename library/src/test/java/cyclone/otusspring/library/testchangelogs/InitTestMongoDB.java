package cyclone.otusspring.library.testchangelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.core.MongoTemplate;

import static cyclone.otusspring.library.TestData.*;

@ChangeLog(order = "001")
public class InitTestMongoDB {

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
        author1 = template.save(AUTHOR1);
        author2 = template.save(AUTHOR2);
        author3 = template.save(AUTHOR3);
    }

    @ChangeSet(order = "002", id = "initGenres", author = "cyclone", runAlways = true)
    public void initGenres(MongoTemplate template) {
        genre1 = template.save(GENRE1);
        genre2 = template.save(GENRE2);
        genre3 = template.save(GENRE3);
        template.save(GENRE4);
    }

    @ChangeSet(order = "003", id = "initBooks", author = "cyclone", runAlways = true)
    public void initBooks(MongoTemplate template) {
        template.save(new Book(BOOK1.getId(), BOOK1.getTitle(), BOOK1.getYear(), author1, genre1));
        template.save(new Book(BOOK2.getId(), BOOK2.getTitle(), BOOK2.getYear(), author1, genre1));
        template.save(new Book(BOOK3.getId(), BOOK3.getTitle(), BOOK3.getYear(), author2, genre2));
        template.save(new Book(BOOK4.getId(), BOOK4.getTitle(), BOOK4.getYear(), author2, genre2));
        template.save(new Book(BOOK5.getId(), BOOK5.getTitle(), BOOK5.getYear(), author3, genre3));
    }


    @ChangeSet(order = "004", id = "initComments", author = "cyclone", runAlways = true)
    public void initComments(MongoTemplate template) {
        //TODO init test comments
    }
}
