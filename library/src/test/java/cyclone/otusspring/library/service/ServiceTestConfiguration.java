package cyclone.otusspring.library.service;

import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan({
        "cyclone.otusspring.library.repository",
        "cyclone.otusspring.library.service"
})
@AutoConfigureDataMongo
@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
public class ServiceTestConfiguration {
}
