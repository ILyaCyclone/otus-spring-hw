package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoGenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAllByOrderByName();

    List<Genre> findByNameContainingIgnoreCaseOrderByName(String name);
}
