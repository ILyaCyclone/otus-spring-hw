package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoAuthorRepository extends MongoRepository<Author, String> {

    @Query(value = "{$or: [ {'firstname' : { $regex: ?0, $options: 'i'}},  {'lastname' : { $regex: ?0, $options: 'i'}}]}"
            , sort = "{firstname: 1, lastname: 1}")
    List<Author> findByName(String name);
}
