package cyclone.otusspring.library.dbteststate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoTestStateConfig {

    @Bean
    MongoTestState mongoTestState() {
        return new MongoTestState();
    }
}
