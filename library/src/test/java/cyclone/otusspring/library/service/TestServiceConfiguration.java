package cyclone.otusspring.library.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootConfiguration
@ComponentScan({
        "cyclone.otusspring.library.repository",
        "cyclone.otusspring.library.service"
})
//@AutoConfigureDataMongo
@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
public class TestServiceConfiguration {
}
