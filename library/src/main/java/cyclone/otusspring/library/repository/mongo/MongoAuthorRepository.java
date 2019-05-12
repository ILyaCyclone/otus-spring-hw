package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoAuthorRepository extends MongoRepository<Author, String> {

    //TODO write query or use criteria
    @Query("{'firstname': ?0}")
//    @Query("{ $or: [ { 'quantity': ?0 }, { 'price': ?0 } ] }")
    List<Author> findByName(String name);
}
