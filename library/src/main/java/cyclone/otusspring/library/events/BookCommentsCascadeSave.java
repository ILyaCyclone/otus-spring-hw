package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Book;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

//import cyclone.otusspring.library.repository.CommentRepository;

/**
 * Mongo event listeners are not actually used in the application but may be used as example.
 */
@Deprecated
//@Component
public class BookCommentsCascadeSave extends AbstractMongoEventListener<Book> {

//    private final CommentRepository commentRepository;

//    public BookCommentsCascadeSave(CommentRepository commentRepository) {
//        this.commentRepository = commentRepository;
//    }


//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<MongoBook> event) {
//        super.onBeforeConvert(event);
//        MongoBook book = event.getSource();
//        book.getComments().stream().filter(e -> Objects.isNull(e.getId())).forEach(commentRepository::save);
//    }
}

