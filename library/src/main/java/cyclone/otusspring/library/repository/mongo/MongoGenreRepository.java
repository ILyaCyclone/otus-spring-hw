package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.mongo.MongoGenre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {
}
