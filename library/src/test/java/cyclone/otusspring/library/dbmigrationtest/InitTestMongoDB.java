package cyclone.otusspring.library.dbmigrationtest;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;

import java.util.HashMap;

import static cyclone.otusspring.library.TestData.*;

@ChangeLog(order = "001")
public class InitTestMongoDB {
    private static final Logger logger = LoggerFactory.getLogger(InitTestMongoDB.class);

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
//        logger.info("mongock initAuthors");
//        template.dropCollection(Author.class);
//        template.createCollection(Author.class);
//        logger.info("mongock authors index info:");
//        template.indexOps(Author.class).getIndexInfo().forEach(indexInfo ->
//                logger.info("- "+indexInfo.toString()));
//        logger.info("----- end of authors indexes");

        author1 = template.save(AUTHOR1);
        author2 = template.save(AUTHOR2);
        author3 = template.save(AUTHOR3);

        //template::save or template::createCollection don't create "author_unique" index from Author @Document @CompoundIndex
        //so recreate index manually
        template.indexOps(Author.class).ensureIndex(new CompoundIndexDefinition(
                new Document(new HashMap() {{
                    put("firstname", 1);
                    put("lastname", 1);
                    put("homeland", 1);
                }})
        )
                .named("mongock_author_unique")
                .unique());
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
