package cyclone.otusspring.library.events;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.GenreService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BookUpdateListener extends AbstractMongoEventListener<Book> {

    private final AuthorService authorService;
    private final GenreService genreService;

    public BookUpdateListener(AuthorService authorService, GenreService genreService) {
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);

        Book book = event.getSource();
        Author author = book.getAuthor();
        Genre genre = book.getGenre();

        Mono.when(authorService.save(author), genreService.save(genre))
                .subscribe();
    }
}
