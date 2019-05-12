package cyclone.otusspring.library.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChangelogsConfig {
    private static final String CHANGELOGS_PACKAGE = "cyclone.otusspring.library.changelogs";

    @Bean
    public Mongock mongock(MongoProperties mongoProperties, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoProperties.getDatabase(), CHANGELOGS_PACKAGE)
                .setLockQuickConfig()
                .build();
    }
}
