package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.mongo.Comment;
import cyclone.otusspring.library.repository.BookRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

@Component
public class BookCommentsCascadeDelete extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;

    public BookCommentsCascadeDelete(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Comment> event) {
        super.onAfterDelete(event);
        Document commentDocument = event.getSource();
        String id = commentDocument.get("_id").toString();
        bookRepository.removeCommentById(id);
    }

}

