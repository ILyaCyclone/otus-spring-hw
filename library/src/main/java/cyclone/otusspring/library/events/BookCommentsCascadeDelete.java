package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Comment;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

/**
 * Mongo event listeners are not actually used in the application but may be used as example.
 */
@Deprecated
//@Component
public class BookCommentsCascadeDelete extends AbstractMongoEventListener<Comment> {

//    private final BookRepository bookRepository;
//
//    public BookCommentsCascadeDelete(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//
//    @Override
//    public void onAfterDelete(AfterDeleteEvent<MongoComment> event) {
//        super.onAfterDelete(event);
//        Document commentDocument = event.getSource();
//        String id = commentDocument.get("_id").toString();
//        bookRepository.removeCommentById(id);
//    }

}

