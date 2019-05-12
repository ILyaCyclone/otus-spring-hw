package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.mongo.MongoBook;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
}
