package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
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

        Object id = event.getSource().get("_id");
        //TODO unblock
        Author author = authorService.findOne(id.toString()).block();

//        List<Book> books = bookService.findByAuthor(author);
        bookService.findByAuthor(author)
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Could not delete author." +
                        "\nReason: author has books. To delete author delete their books first.")))
                .subscribe();

//        if (!books.isEmpty()) {
//            throw new DataIntegrityViolationException("Could not delete author." +
//                    "\nReason: author has books. To delete author delete their books first.");
//        }

        super.onBeforeDelete(event);
    }
}
