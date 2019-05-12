package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.mongo.MongoAuthor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String> {
}
