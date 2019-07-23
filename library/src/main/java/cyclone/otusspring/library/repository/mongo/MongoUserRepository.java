package cyclone.otusspring.library.repository.mongo;

import cyclone.otusspring.library.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoUserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

}
