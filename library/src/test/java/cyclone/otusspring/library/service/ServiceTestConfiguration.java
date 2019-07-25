package cyclone.otusspring.library.service;

import cyclone.otusspring.library.security.SecurityConfig;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan({
        "cyclone.otusspring.library.repository",
        "cyclone.otusspring.library.events",
        "cyclone.otusspring.library.mapper",
        "cyclone.otusspring.library.service"
})
@AutoConfigureDataMongo
@Import(SecurityConfig.class) // required for AuthenticationService
@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository.mongo")
public class ServiceTestConfiguration {
}
