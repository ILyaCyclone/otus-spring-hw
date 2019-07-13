package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import org.reactivestreams.Subscriber;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AuthorDeleteListener extends AbstractMongoEventListener<Author> {


    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorDeleteListener(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        Mono.just(event)
                .map(ev -> ev.getSource().get("_id").toString())
                .flatMap(authorService::findOne)
                .flatMapMany(bookService::findByAuthor)
                .switchIfEmpty(Subscriber::onComplete) // proceed if author does not have books
                // throw error if author has books
                .flatMap(book -> Flux.error(new DataIntegrityViolationException("Could not delete author." +
                        "\nReason: author has books. To delete author delete their books first.")))
//                .subscribe(); // doesn't work
                .then().block(); // block is needed, event listener is synchronous (?)
    }
}
