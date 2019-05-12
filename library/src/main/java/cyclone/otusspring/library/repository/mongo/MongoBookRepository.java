package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoBookRepository extends MongoRepository<Book, String> {

    List<Book> findAllByOrderByTitle();

    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
