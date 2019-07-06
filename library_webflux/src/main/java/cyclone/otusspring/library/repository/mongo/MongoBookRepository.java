package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoBookRepository extends MongoRepository<Book, String> {

    List<Book> findAllByOrderByTitle();

    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);

    List<Book> findByAuthorOrderByTitle(Author author);

    List<Book> findByGenreOrderByTitle(Genre genre);

}
