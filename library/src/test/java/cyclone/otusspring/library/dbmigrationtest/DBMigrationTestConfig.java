package cyclone.otusspring.library.dbmigrationtest;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBMigrationTestConfig {
    private static final String CHANGELOGS_PACKAGE = "cyclone.otusspring.library.dbmigrationtest";

    @Bean
    public Mongock mongock(MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, "otus-library-test", CHANGELOGS_PACKAGE)
                .setLockQuickConfig()
                .build();
    }
}
