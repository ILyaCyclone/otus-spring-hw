package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreDeleteListener extends AbstractMongoEventListener<Genre> {

    private final GenreService genreService;
    private final BookService bookService;

    public GenreDeleteListener(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {

        Object id = event.getSource().get("_id");
        //TODO unblock
        Genre genre = genreService.findOne(id.toString()).block();
        //TODO unblock
        List<Book> books = bookService.findByGenre(genre).collectList().block();

        if (!books.isEmpty()) {
            throw new DataIntegrityViolationException("Could not delete genre." +
                    "\nReason: genre has books. To delete genre delete its books first.");
        }

        super.onBeforeDelete(event);
    }
}
