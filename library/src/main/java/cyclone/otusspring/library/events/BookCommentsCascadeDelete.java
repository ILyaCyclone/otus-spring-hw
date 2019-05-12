package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.mongo.MongoComment;
import cyclone.otusspring.library.repository.BookRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@Component
public class BookCommentsCascadeDelete extends AbstractMongoEventListener<MongoComment> {

    private final BookRepository bookRepository;

    public BookCommentsCascadeDelete(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

//    @Override
//    public void onAfterDelete(AfterDeleteEvent<MongoComment> event) {
//        super.onAfterDelete(event);
//        Document commentDocument = event.getSource();
//        String id = commentDocument.get("_id").toString();
//        bookRepository.removeCommentById(id);
//    }

}

