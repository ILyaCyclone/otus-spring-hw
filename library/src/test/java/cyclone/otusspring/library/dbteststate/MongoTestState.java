package cyclone.otusspring.library.dbteststate;

import com.mongodb.client.model.Filters;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static cyclone.otusspring.library.TestData.*;

@Component
public class MongoTestState {

    @Autowired
    MongoTemplate template;

    public void resetState() {
//        template.indexOps(Author.class).getIndexInfo().forEach(indexInfo ->
//                logger.info("- "+indexInfo.toString()));
//        logger.info("----- end of authors indexes");

        clearCollection(Comment.class);
        clearCollection(Book.class);
        clearCollection(Genre.class);
        clearCollection(Author.class);


        Author author1 = template.save(AUTHOR1);
        Author author2 = template.save(AUTHOR2);
        Author author3 = template.save(AUTHOR3);


        Genre genre1 = template.save(GENRE1);
        Genre genre2 = template.save(GENRE2);
        Genre genre3 = template.save(GENRE3);
        template.save(GENRE4);


        template.save(new Book(BOOK1.getId(), BOOK1.getTitle(), BOOK1.getYear(), author1, genre1));
        template.save(new Book(BOOK2.getId(), BOOK2.getTitle(), BOOK2.getYear(), author1, genre1));
        template.save(new Book(BOOK3.getId(), BOOK3.getTitle(), BOOK3.getYear(), author2, genre2));
        template.save(new Book(BOOK4.getId(), BOOK4.getTitle(), BOOK4.getYear(), author2, genre2));
        template.save(new Book(BOOK5.getId(), BOOK5.getTitle(), BOOK5.getYear(), author3, genre3));


        //TODO init test comments
    }

    /**
     * Remove all documents from collection, but preserve indexes
     */
    private void clearCollection(Class entityClass) {
        // unlike MongoTemplate::dropCollection this will preserve indexes
        template.getCollection(template.getCollectionName(entityClass)).deleteMany(Filters.exists("_id"));
        // another variant
//        template.remove(new Query(Criteria.where("_id").exists(true)), entityClass);
    }


    //template::save or template::createCollection don't create "authors_unique" index from Author @Document @CompoundIndex
        //so recreate index manually
//        template.indexOps(Author.class).ensureIndex(new CompoundIndexDefinition(
//                new Document(new HashMap() {{
//                    put("firstname", 1);
//                    put("lastname", 1);
//                    put("homeland", 1);
//                }})
//        )
//                .named("manual_authors_unique")
//                .unique());
}
