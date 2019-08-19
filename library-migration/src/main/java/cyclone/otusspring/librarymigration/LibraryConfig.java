package cyclone.otusspring.librarymigration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * initialize beans from original library application
 */

@Configuration
@ComponentScan({
        "cyclone.otusspring.library.repository"
})
@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
public class LibraryConfig {

}
