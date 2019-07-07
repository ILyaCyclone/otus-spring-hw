package cyclone.otusspring.library.service;

import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@ComponentScan({
        "cyclone.otusspring.library.repository",
        "cyclone.otusspring.library.events",
        "cyclone.otusspring.library.mapper",
        "cyclone.otusspring.library.service"
})
@AutoConfigureDataMongo
@EnableReactiveMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
public class ServiceTestConfiguration {
}
