package cyclone.otusspring.library.dbteststate;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Clears test MongoDB state and recreates test records. Run before each test method.
 */
public class ResetStateExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        MongoTemplate mongoTemplate = SpringExtension.getApplicationContext(context).getBean(MongoTemplate.class);
        new MongoTestState(mongoTemplate).resetState();
    }
}
