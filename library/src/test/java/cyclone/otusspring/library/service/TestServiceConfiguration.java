package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dao.DataAccessProfiles;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootConfiguration
@EnableAutoConfiguration
//@EnableConfigurationProperties (if needed)
@ComponentScan({"cyclone.otusspring.library.dao.jpa", "cyclone.otusspring.library.service"})
@EntityScan({"cyclone.otusspring.library.model"})
public class TestServiceConfiguration {
}
