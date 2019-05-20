package cyclone.otusspring.library.dbteststate;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.core.MongoTemplate;

import static cyclone.otusspring.library.TestData.*;

public class MongoTestState {

    private final MongoTemplate mongoTemplate;

    public MongoTestState(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public void resetState() {
        clearState();
        fillState();
    }



    private void clearState() {
        clearCollection(Comment.class);
        clearCollection(Book.class);
        clearCollection(Genre.class);
        clearCollection(Author.class);
    }

    private void fillState() {
        Author author1 = mongoTemplate.save(AUTHOR1);
        Author author2 = mongoTemplate.save(AUTHOR2);
        Author author3 = mongoTemplate.save(AUTHOR3);
        mongoTemplate.save(AUTHOR_WITHOUT_BOOKS);


        Genre genre1 = mongoTemplate.save(GENRE1);
        Genre genre2 = mongoTemplate.save(GENRE2);
        Genre genre3 = mongoTemplate.save(GENRE3);
        mongoTemplate.save(GENRE4);


        Book book1 = new Book(BOOK1.getId(), BOOK1.getTitle(), BOOK1.getYear(), author1, genre1);
        book1.addComment(COMMENT1);
        book1.addComment(COMMENT3);

        Book book2 = new Book(BOOK2.getId(), BOOK2.getTitle(), BOOK2.getYear(), author1, genre1);
        book2.addComment(COMMENT2);
        book2.addComment(COMMENT4);

        Book book3 = new Book(BOOK3.getId(), BOOK3.getTitle(), BOOK3.getYear(), author2, genre2);
        book3.addComment(COMMENT5);

        mongoTemplate.save(book1);
        mongoTemplate.save(book2);
        mongoTemplate.save(book3);
        mongoTemplate.save(new Book(BOOK4.getId(), BOOK4.getTitle(), BOOK4.getYear(), author2, genre2));
        mongoTemplate.save(new Book(BOOK5.getId(), BOOK5.getTitle(), BOOK5.getYear(), author3, genre3));
    }

    /**
     * Remove all documents from collection, but preserve indexes
     */
    private void clearCollection(Class entityClass) {
        // unlike MongoTemplate::dropCollection, this method will preserve indexes
        mongoTemplate.findAll(entityClass).forEach(mongoTemplate::remove);
        // ugly variant 1
//        mongoTemplate.getCollection(mongoTemplate.getCollectionName(entityClass)).deleteMany(Filters.exists("_id"));
        // ugly variant 2
//        mongoTemplate.remove(new Query(Criteria.where("_id").exists(true)), entityClass);
    }


    //mongoTemplate::save or mongoTemplate::createCollection don't create "authors_unique" index from Author @Document @CompoundIndex
    //so recreate index manually
//        mongoTemplate.indexOps(Author.class).ensureIndex(new CompoundIndexDefinition(
//                new Document(new HashMap() {{
//                    put("firstname", 1);
//                    put("lastname", 1);
//                    put("homeland", 1);
//                }})
//        )
//                .named("manual_authors_unique")
//                .unique());
}
