package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.reactivestreams.Subscriber;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        Mono.just(event)
                .map(ev -> ev.getSource().get("_id").toString())
                .flatMap(genreService::findOne)
                .flatMapMany(bookService::findByGenre)
                .switchIfEmpty(Subscriber::onComplete) // proceed if genre does not have books
                // throw error if genre has books
                .flatMap(book -> Flux.error(new DataIntegrityViolationException("Could not delete genre." +
                        "\nReason: genre has books. To delete genre delete its books first.")))
                .then().block();
    }
}
