package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.mongo.Book;
import cyclone.otusspring.library.repository.CommentRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookCommentsCascadeSave extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    public BookCommentsCascadeSave(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        Book book = event.getSource();
        book.getComments().stream().filter(e -> Objects.isNull(e.getId())).forEach(commentRepository::save);
    }
}

